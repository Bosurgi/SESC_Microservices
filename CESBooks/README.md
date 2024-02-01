# Library Portal
A microservices-based web application to manage a university library.
The application is written in Python using the Flask framework.

![component diagram](static/library.png "Component Diagram")

## Library Student Features
1. Login - secure password-verified login.
2. Books - display all books in the library.
3. Borrow - borrow a book using barcode scanner.
4. Return - return a book using thebarcode scanner.
5. Account - display the user's borrowng history.

## Library Admin Features
1. Add Title - add new books to the database using the barcode scanner.
2. Books - display all books in the library.
3. Students - dispay all students, and the number of books each has on loan/overdue.
4. Current Loans - display all books currently on loan.
5. Overdue - display all books that are overdue.

## API
A REST API is exposed which allows other applications to create a new library account.
POST requests should be sent to /api/register, containing a JSON body { "studentId": "cXXXXXXX" } where cXXXXXXX is the student id.
This results in a new library account with the default PIN '000000'.
Upon logging in for the first time, the new student will be asked to update this PIN.

## Integrations
### 1. Database
The application integrates with a MySQL relational database.<br/>
The connection string is set in the application.py file.<br/>
Scripts to create the database schema can be found in the migrations folder.

### 2. Finance
The application integrates with the [Finance microservice](https://github.com/tvergilio/finance) via REST.
1. When a book is returned late, a fine is issued. A request is sent to the Finance microservice to create an invoice. 
2. The invoice must be paid via the Payment Portal.

### 3. Student
The application integrates with the [Student microservice](https://github.com/tvergilio/student) via REST.
1. When a student is created via the Student microservice, a request is sent to this application to create an account for the student.
2. The default library pin for all new accounts is '000000' (see API above).

## Demos
[![Watch the demo.](http://img.youtube.com/vi/6Z2XXVkB3gk/hqdefault.jpg)](https://youtu.be/6Z2XXVkB3gk)

## Run using Docker Compose
1. Ensure the Finance and Student microservices are running, or the integrations will not work. **Finance must be started first.**<br/>
2. Rename the `.env.example` file inside the application root directory to `.env`:<br/>
3. Edit any credentials in the `.env` file as needed (do not rename the DB_NAME).<br/>
4. From the application root directory, run the libraryapp and librarydb services:<br/>
   `docker-compose up`

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.
Please make sure to update tests as appropriate.

## License
Copyright (c) 2020/21 A. Curley

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
