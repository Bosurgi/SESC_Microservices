import unittest
from classes.fine import Fine
from datetime import datetime, date

STUDENT_ID = 'c12312312'
BORROWED_DATE = datetime.strptime('2020-04-01', '%Y-%m-%d').date()


class TestFine(unittest.TestCase):
    def test_create_fine_with_valid_args_creates_Fine(self):
        test_fine = Fine(BORROWED_DATE, STUDENT_ID)
        self.assertEqual(test_fine.type, 'LIBRARY_FINE')
        self.assertEqual(test_fine.student_id, {'studentId': str(STUDENT_ID)})

    def test_calculate_fine_with_borrowed_equals_today_raises_ValueError(self):
        try:
            Fine.calculate_fine(date.today())
        except ValueError as e:
            self.assertEqual(type(e), ValueError)
            self.assertEqual('No fine necessary.', str(e))
        else:
            self.fail('ValueError not raised')


if __name__ == '__main__':
    unittest.main()
