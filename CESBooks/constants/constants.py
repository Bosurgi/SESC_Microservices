import re

FINE_PER_DAY = .50
MAX_BORROWING_DURATION = 14
MAX_PAYMENT_DURATION = 14
MAX_BOOKS_ALLOWED = 5

NO_DATE = '0000:00:00'
DATE_DISPLAY_FORMAT = '%d-%b-%Y'
DATE_DB_FORMAT = '%Y-%m-%d'

DEFAULT_PIN = "000000"

INVOICES_URL = "http://financeapp:8081/invoices"

ISBN_PATTERN = re.compile(r'[\d]{13}')
PIN_PATTERN = re.compile(r'[\d]{6}')
