import unittest
from helpers import *
from datetime import datetime, date, timedelta
from constants.constants import MAX_BORROWING_DURATION, DEFAULT_PIN

STUDENT_ID = 'c12312312'
BORROWED_DATE = datetime.strptime('2020-04-01', '%Y-%m-%d').date()


class TestHelpers(unittest.TestCase):
    def test_days_before_with_date1_one_year_earlier_expect_365(self):
        result = days_before(datetime.strptime('2021-04-01', '%Y-%m-%d').date(),
                             datetime.strptime('2022-04-01', '%Y-%m-%d').date())
        self.assertEqual(result, 365)

    def test_days_before_with_equal_dates_expect_0(self):
        result = days_before(date.today(), date.today())
        self.assertEqual(result, 0)

    def test_days_before_with_date1_one_year_later_expect_0(self):
        result = days_before(datetime.strptime('2022-04-01', '%Y-%m-%d').date(),
                             datetime.strptime('2021-04-01', '%Y-%m-%d').date())
        self.assertEqual(result, 0)

    def test_calculate_days_overdue_with_borrowed_and_returned_on_same_day_returns_empty_string(self):
        result = calculate_days_overdue(date.today(), date.today())
        self.assertEqual(result, '')

    def test_calculate_days_overdue_with_borrowed_today_not_returned_returns_empty_string(self):
        result = calculate_days_overdue(date.today(), None)
        self.assertEqual(result, '')

    def test_calculate_days_overdue_with_borrowed_and_returned_within_MAX_BORROWING_DURATION_returns_empty_string(self):
        result = calculate_days_overdue(date.today() - timedelta(days=MAX_BORROWING_DURATION), date.today())
        self.assertEqual(result, '')

    def test_calculate_days_overdue_with_borrowed_1_year_before_today_returns_365_minus_MAX_BORROWING_DURATION(self):
        result = calculate_days_overdue(date.today() - timedelta(days=365), None)
        self.assertEqual(result, f'{365-MAX_BORROWING_DURATION} days')

    def test_is_valid_isbn_with_valid_isbn_returns_isbn(self):
        result = is_valid_isbn('9781231231231')
        self.assertEqual(result, '9781231231231')

    def test_is_valid_isbn_with_short_isbn_raises_ValueError(self):
        try:
            is_valid_isbn('978')
        except ValueError as e:
            self.assertEqual(type(e), ValueError)
            self.assertEqual('The ISBN must contain 13 digits.', str(e))
        else:
            self.fail('ValueError not raised.')

    def test_is_valid_year_with_valid_year_as_string_of_digits_returns_year(self):
        result = is_valid_year('2020')
        self.assertEqual(result, '2020')

    def test_is_valid_year_with_valid_year_as_integer_returns_year(self):
        result = is_valid_year(2020)
        self.assertEqual(result, 2020)

    def test_is_valid_year_with_year_in_the_future_raises_ValueError(self):
        try:
            is_valid_year(date.today().year + 1)
        except ValueError as e:
            self.assertEqual(type(e), ValueError)
            self.assertEqual('Invalid year.', str(e))
        else:
            self.fail('ValueError not raised.')

    def test_is_valid_year_with_year_in_letters_raises_ValueError(self):
        try:
            is_valid_year('TwentyTwenty')
        except ValueError as e:
            self.assertEqual(type(e), ValueError)
            self.assertEqual('Invalid year.', str(e))
        else:
            self.fail('ValueError not raised.')

    def test_is_valid_year_with_year_as_negative_integer_raises_ValueError(self):
        try:
            is_valid_year(-2020)
        except ValueError as e:
            self.assertEqual(type(e), ValueError)
            self.assertEqual('Invalid year.', str(e))
        else:
            self.fail('ValueError not raised.')

    def test_is_valid_num_copies_with_1_returns_1(self):
        result = is_valid_num_copies(1)
        self.assertEqual(result, 1)

    def test_is_valid_num_copies_with_0_returns_0(self):
        result = is_valid_num_copies(0)
        self.assertEqual(result, 0)

    def test_is_valid_num_copies_with_negative_integer_raises_ValueError(self):
        try:
            is_valid_num_copies(-1)
        except ValueError as e:
            self.assertEqual(type(e), ValueError)
            self.assertEqual('Invalid number of copies.', str(e))
        else:
            self.fail('ValueError not raised.')

    def test_is_valid_num_copies_with_over_100_raises_ValueError(self):
        try:
            is_valid_num_copies(101)
        except ValueError as e:
            self.assertEqual(type(e), ValueError)
            self.assertEqual('Invalid number of copies.', str(e))
        else:
            self.fail('ValueError not raised.')

    def test_is_valid_pin_with_123456_returns_123456(self):
        result = is_valid_pin('123456')
        self.assertEqual(result, '123456')

    def test_is_valid_pin_with_default_pin_raises_ValueError(self):
        try:
            is_valid_pin(DEFAULT_PIN)
        except ValueError as e:
            self.assertEqual(type(e), ValueError)
            self.assertEqual('You cannot use the default PIN.', str(e))
        else:
            self.fail('ValueError not raised.')

    def test_is_valid_pin_with_short_pin_raises_ValueError(self):
        try:
            is_valid_pin('12345')
        except ValueError as e:
            self.assertEqual(type(e), ValueError)
            self.assertEqual('The PIN must be 6 digits long.', str(e))
        else:
            self.fail('ValueError not raised.')

    def test_is_valid_pin_with_long_pin_raises_ValueError(self):
        try:
            is_valid_pin('1234567')
        except ValueError as e:
            self.assertEqual(type(e), ValueError)
            self.assertEqual('The PIN must be 6 digits long.', str(e))
        else:
            self.fail('ValueError not raised.')

    def test_is_valid_pin_with_invalid_string_raises_ValueError(self):
        try:
            is_valid_pin('secret')
        except ValueError as e:
            self.assertEqual(type(e), ValueError)
            self.assertEqual('The PIN must be 6 digits long.', str(e))
        else:
            self.fail('ValueError not raised.')



if __name__ == '__main__':
    unittest.main()
