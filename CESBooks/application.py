import os
import isbnlib
import requests

from datetime import timedelta

from cs50 import SQL
from flask import Flask, flash, request
from flask_session import Session
from tempfile import mkdtemp

from werkzeug.exceptions import default_exceptions, HTTPException, InternalServerError
from werkzeug.security import check_password_hash, generate_password_hash

# Local imports
from classes.book import Book
from classes.transaction import Transaction
from classes.fine import Fine

from constants import constants, messages
from errors.errors import IncompleteBookError, NotValidISBNError

from helpers import *


app = Flask(__name__)

# Ensure templates are auto-reloaded
app.config['TEMPLATES_AUTO_RELOAD'] = True


# Ensure responses aren't cached
@app.after_request
def after_request(response):
    db.execute('COMMIT;')
    response.headers['Cache-Control'] = 'no-cache, no-store, must-revalidate'
    response.headers['Expires'] = 0
    response.headers['Pragma'] = 'no-cache'
    return response


# Configure session to use filesystem (instead of signed cookies)
app.config['SESSION_FILE_DIR'] = mkdtemp()
app.config['SESSION_PERMANENT'] = False
app.config['SESSION_TYPE'] = 'filesystem'
Session(app)

# Configure CS50 Library to use SQL database
# db = SQL("mysql://redyelruc:financered180974finance@127.0.0.1:3309/finance")
db = SQL(os.environ['DATABASE'])


@app.route('/api/register', methods=['POST'])
def register():
    """Register a new student"""
    student_id = request.json['studentId']
    # check student_id is not already registered
    if db.execute('SELECT * FROM student WHERE id = %s', student_id):
        return {'studentId': student_id}

    db.execute('INSERT INTO student(id, hash) VALUES (%s, %s)',
               student_id, generate_password_hash(constants.DEFAULT_PIN))
    return {'studentId': student_id}


@app.route('/login', methods=['GET', 'POST'])
def login():
    """Log user in"""
    # Forget any user_id
    session.clear()

    if request.method == 'POST':

        student_id = request.form.get('student_id')
        pin = request.form.get('pin')
        if not student_id or not pin:
            return apology('incomplete details', 403)

        # Ensure student_id exists and password is correct
        rows = db.execute('SELECT * FROM student WHERE id = %s', student_id)
        if len(rows) != 1 or not check_password_hash(rows[0]['hash'], pin):
            return apology('invalid details', 403)

        # If this is first time the student has logged in, force them to change the pin
        if pin == constants.DEFAULT_PIN:
            session['new_user'] = student_id
            return redirect('/firstlogin')

        if student_id == 'admin':
            session['admin'] = True
            return redirect('/admin/books')

        session['user_id'] = student_id
        return redirect('/')

    else:
        return render_template('user/login.html')


@app.route('/firstlogin', methods=['GET', 'POST'])
def firstlogin():
    """Update Password"""
    if request.method == 'GET':
        return render_template('user/firstlogin.html', student_id=session['new_user'])
    else:
        pin = request.form.get('pin')
        pin_confirmation = request.form.get('confirm-pin')

        try:
            is_valid_pin(pin)
        except ValueError as e:
            flash(str(e), 'alert-danger')
            return redirect('/firstlogin')

        if pin != pin_confirmation:
            flash(messages.PIN_CONFIRMATION_ERROR, 'alert-danger')
            return redirect('/firstlogin')
        else:
            # save new pin to the db and log the student in
            session['new_user'] = None
            session['user_id'] = request.form.get('student_id')
            db.execute('UPDATE student SET hash = %s WHERE id = %s', generate_password_hash(pin), session['user_id'])
            flash(messages.PIN_UPDATE_SUCCESS, 'alert-success')
            return redirect('/')


@app.route('/logout')
def logout():
    """Log user out"""

    # Forget any user_id
    session.clear()

    return redirect('/')


@app.route('/', methods=['GET'])
@login_required
def books():
    """List all books"""
    rows = db.execute("SELECT * FROM book ORDER BY title")
    booklist = []

    for row in rows:
        booklist.append([row['isbn'],
                         row['title'],
                         row['author'],
                         row['year'],
                         row['copies']])

    if request.method == 'GET':
        return render_template('user/books.html', books=booklist, title='All Titles')


@app.route('/history')
@login_required
def transactions():
    """Show history of student's loans and returns"""
    rows = db.execute(
        'SELECT * FROM transaction WHERE student_id = %s ORDER BY date_returned ASC, date_borrowed DESC LIMIT 8',
        session['user_id'])
    transaction_history = []
    for row in rows:
        if row['date_returned']:
            row['date_returned'] = row['date_returned'].strftime(constants.DATE_DISPLAY_FORMAT)

        transaction_history.append([row['book_isbn'],
                                    row['date_borrowed'].strftime(constants.DATE_DISPLAY_FORMAT),
                                    row['date_returned'],
                                    calculate_days_overdue(row['date_borrowed'], row['date_returned'])])

    return render_template('user/transactions.html', transactions=transaction_history, title='My Account')


@app.route('/borrow', methods=['GET', 'POST'])
@login_required
def borrow():
    """Borrow a book"""
    if request.method == 'GET':
        return render_template('user/borrow.html', books=books)
    else:
        isbn = request.form.get('isbn')
        today = date.today().strftime(constants.DATE_DB_FORMAT)
        book = select_book(isbn)

        if not book:
            flash(messages.BOOK_NOT_FOUND_ERROR, 'alert-danger')
            return redirect('/')

        if book.copies == 0:
            flash(messages.NO_COPIES_AVAILABLE, 'alert-danger')
            return redirect('/')
        else:
            db.execute("UPDATE book SET copies = copies -1 WHERE isbn = %s", isbn)
            db.execute("INSERT INTO transaction (student_id,book_isbn,date_borrowed, date_returned) "
                       "VALUES (%s, %s, %s, %s)", session["user_id"], isbn, today, constants.NO_DATE)
            message = f"You have borrowed '{book.title}' until " \
                      f"{(date.today() + timedelta(days=14)).strftime(constants.DATE_DISPLAY_FORMAT)}."

        flash(message, 'alert-success')
        return redirect('/history')


@app.route('/return_books', methods=['GET', 'POST'])
@login_required
def return_books():
    """Return a book"""
    if request.method == 'GET':
        return render_template('user/return.html')
    else:
        today = date.today().strftime(constants.DATE_DB_FORMAT)
        book = select_book(request.form.get('isbn'))

        if not book:
            flash(messages.BOOK_NOT_FOUND_ERROR, 'alert-danger')
            return redirect('/')

        # RETURN A BOOK
        try:
            records = db.execute(
                "SELECT * FROM transaction WHERE book_isbn = %s AND student_id = %s AND date_returned = %s",
                book.isbn, session['user_id'], constants.NO_DATE)

            if len(records) < 1:
                flash(messages.NOT_BORROWED, 'alert-danger')
                return redirect('/history')

            transaction = Transaction(records[0])

            db.execute("UPDATE transaction SET date_returned = %s WHERE id = %s", today, transaction.id)
            db.execute("UPDATE book SET copies = copies + 1 WHERE isbn = %s", book.isbn)
            message = messages.BOOK_RETURNED_SUCCESS

        except Exception as e:
            flash(str(e), 'alert-danger')

        # Isolate and identify api connection issues if they exist
        try:
            # check, and issue fine if needed
            if days_before(transaction.date_borrowed, date.today()) > constants.MAX_BORROWING_DURATION:
                fine = Fine(transaction.date_borrowed, session['user_id'])

                # Call out to API
                r = requests.post(constants.INVOICES_URL, json=fine.details)
                message += f"{messages.FINE_ISSUED} £{'{:.2f}'.format(fine.amount)}. " \
                           f"{messages.VISIT_PAYMENT_PORTAL} {r.json()['reference']}."
        except requests.exceptions.ConnectionError:
            flash(f'{messages.BOOK_RETURNED_SUCCESS} {messages.API_CONNECTION_ERROR}', 'alert-danger')
            return redirect('/history')

        flash(message, 'alert-success')

    return redirect('/history')


@app.route('/admin/books', methods=['GET'])
@admin_required
def admin_books():
    """List all books"""
    rows = db.execute('SELECT * FROM book ORDER BY title')
    booklist = []
    cols = [('ISBN', 'normal-width'),
            ('Title', 'wide-width'),
            ('Author', 'normal-width'),
            ('Year', 'narrow-width'),
            ('Copies', 'narrow-width')]

    for row in rows:
        booklist.append([row['isbn'],
                         row['title'],
                         row['author'],
                         row['year'],
                         row['copies']])

    return render_template('admin/admin_books.html', title='All Titles', cols=cols, books=booklist)


@app.route('/admin/students', methods=["GET"])
@admin_required
def admin_students():
    """List all students, number of current loans and overdue books"""
    students = db.execute("SELECT id FROM student WHERE id != 'admin' ORDER BY id")
    student_list = []

    for student in students:
        current_loans = db.execute('SELECT count(*) FROM transaction WHERE student_id = %s AND date_returned = %s',
                                   student['id'], constants.NO_DATE)
        borrowed = (date.today() - timedelta(days=constants.MAX_BORROWING_DURATION))
        overdue = db.execute('SELECT count(*) FROM transaction WHERE student_id = %s AND '
                             'date_borrowed < %s AND date_returned = %s', student['id'], borrowed, constants.NO_DATE)

        student_list.append([student['id'],
                             current_loans[0]['count(*)'],
                             overdue[0]['count(*)']])

    return render_template('admin/admin_students.html', title='All Students', students=student_list)


@app.route('/admin/loans', methods=['GET'])
@admin_required
def admin_loans():
    """List all titles currently out on loan"""
    loans = db.execute('SELECT book_isbn, book.title, student_id, date_borrowed FROM transaction JOIN '
                       'book ON transaction.book_isbn = book.isbn WHERE date_returned = %s', constants.NO_DATE)

    cols = [('ISBN', 'normal-width'),
            ('Title', 'wide-width'),
            ('Student', 'normal-width'),
            ('Borrowed', 'normal-width')]

    loan_list = []
    for book in loans:
        loan_list.append([book['book_isbn'], book['title'], book['student_id'],
                          book['date_borrowed'].strftime(constants.DATE_DISPLAY_FORMAT)])

    return render_template('admin/admin_books.html', title='Current Loans', cols=cols, books=loan_list)


@app.route('/admin/overdue', methods=['GET'])
@admin_required
def admin_overdue():
    """List all titles currently overdue"""
    borrowed = (date.today() - timedelta(days=constants.MAX_BORROWING_DURATION))
    overdue = db.execute('SELECT book_isbn, book.title, student_id, date_borrowed FROM transaction JOIN '
                         'book ON transaction.book_isbn = book.isbn WHERE date_borrowed < %s AND date_returned = %s',
                         borrowed, constants.NO_DATE)

    cols = [('ISBN', 'normal-width'),
            ('Title', 'wide-width'),
            ('Student', 'normal-width'),
            ('Borrowed', 'normal-width')]

    overdue_list = []
    for book in overdue:
        overdue_list.append([book['book_isbn'],
                             book['title'],
                             book['student_id'],
                             book['date_borrowed'].strftime(constants.DATE_DISPLAY_FORMAT)])

    return render_template('admin/admin_books.html', title='Overdue Books', cols=cols, books=overdue_list)


@app.route('/admin/add', methods=['GET', 'POST'])
@admin_required
def add():
    """Add a new title to the library by ISBN."""
    if request.method == 'GET':
        return render_template('admin/add.html')
    else:
        isbn = request.form.get('isbn')
        try:
            is_valid_isbn(isbn)
            # Use isbnlib to look up the isbn and get all relevant details
            details = isbnlib.meta(isbn)
            cover = isbnlib.cover(isbn)

            book_details = {'isbn': details['ISBN-13'],
                            'title': details['Title'],
                            'author': details['Authors'][0],
                            'year': details['Year'],
                            'cover': cover['thumbnail'],
                            'copies': 1}
            return render_template('admin/addstock.html', **book_details)

        except (ValueError, isbnlib._exceptions.NotValidISBNError) as e:
            flash(e, 'alert-danger')
            return redirect('/admin/add')


@app.route('/admin/addstock', methods=['POST'])
@admin_required
def addstock():
    """Check/complete new title's details before adding to the library db"""
    try:
        book = Book(request.form.get('isbn'),
                    request.form.get('title'),
                    request.form.get('author'),
                    request.form.get('year'),
                    request.form.get('copies'))
        db.execute('INSERT INTO book(isbn, title, author, year, copies) VALUES(%s, %s, %s, %s, %s)',
                   book.isbn, book.title, book.author, book.year, book.copies)

        flash(f'{book.title}{messages.BOOK_ADDED_SUCCESS}', 'alert-success')
        return redirect('/admin/books')

    except (IncompleteBookError, NotValidISBNError, ValueError) as e:
        flash(f'{e}{messages.BOOK_NOT_ADDED_ERROR}', 'alert-danger')
        return redirect('/admin/add')


def errorhandler(e):
    """Handle error"""
    if not isinstance(e, HTTPException):
        e = InternalServerError()
    return apology(e.name, e.code)


def select_book(isbn):
    """Select a single record from the book table by isbn"""
    book_records = db.execute('SELECT * FROM book WHERE isbn = %s LIMIT 1', isbn)
    return None if not book_records else Book(book_records[0])


# Listen for errors
for code in default_exceptions:
    app.errorhandler(code)(errorhandler)
