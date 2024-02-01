import re
from datetime import date
from functools import wraps
from flask import redirect, render_template, session
from constants.constants import ISBN_PATTERN, PIN_PATTERN, MAX_BORROWING_DURATION, DEFAULT_PIN


def apology(message, code=400):
    """Render message as an apology to user."""
    def escape(s):
        """
        Escape special characters.

        https://github.com/jacebrowning/memegen#special-characters
        """
        for old, new in [("-", "--"), (" ", "-"), ("_", "__"), ("?", "~q"),
                         ("%", "~p"), ("#", "~h"), ("/", "~s"), ("\"", "''")]:
            s = s.replace(old, new)
        return s
    return render_template("user/apology.html", top=code, bottom=escape(message)), code


def login_required(f):
    @wraps(f)
    def decorated_function(*args, **kwargs):
        if session.get("user_id") is None:
            return redirect("/login")
        return f(*args, **kwargs)
    return decorated_function


def admin_required(f):
    @wraps(f)
    def decorated_function(*args, **kwargs):
        if session.get("admin") is None:
            return redirect("/login")
        return f(*args, **kwargs)
    return decorated_function


def days_before(d1, d2):
    if d1 < d2:
        return (d2 - d1).days
    else:
        return 0


def calculate_days_overdue(date_borrowed, date_returned):
    if date_returned is not None:
        return ''
    days_borrowed = days_before(date_borrowed, date.today())
    if days_borrowed < MAX_BORROWING_DURATION:
        return ''
    days_overdue = days_borrowed - MAX_BORROWING_DURATION
    return f'{days_overdue} day' if days_overdue == 1 else f'{days_overdue} days'


def is_valid_isbn(isbn):
    if re.fullmatch(ISBN_PATTERN, isbn) is None:
        raise ValueError('The ISBN must contain 13 digits.')
    else:
        return isbn


def is_valid_pin(pin):
    if pin == DEFAULT_PIN:
        raise ValueError('You cannot use the default PIN.')
    if re.fullmatch(PIN_PATTERN, pin) is None:
        raise ValueError('The PIN must be 6 digits long.')
    else:
        return pin


def is_valid_year(year):
    try:
        year_int = int(year)
    except ValueError:
        raise ValueError('Invalid year.')
    if 1500 <= year_int <= date.today().year:
        return year
    else:
        raise ValueError('Invalid year.')


def is_valid_num_copies(copies):
    try:
        num_copies = int(copies)
    except ValueError:
        raise ValueError('Invalid number of copies.')
    if 0 <= num_copies <= 99:
        return copies
    else:
        raise ValueError('Invalid number of copies.')


