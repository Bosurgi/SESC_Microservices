from datetime import timedelta, date
from helpers import days_before
from constants.constants import *


class Fine:
    def __init__(self, borrowed, student_id):
        self.days_borrowed = days_before(borrowed, date.today())
        self.amount = self.calculate_fine(borrowed)
        self.due = (date.today() + timedelta(days=MAX_PAYMENT_DURATION)).strftime("%Y-%m-%d")
        self.type = 'LIBRARY_FINE'
        self.student_id = {'studentId': str(student_id)}
        self.details = {'amount': self.amount, 'dueDate': self.due, 'type': self.type, 'account': self.student_id}

    @staticmethod
    def calculate_fine(borrowed):
        days_late = days_before(borrowed + timedelta(days=MAX_BORROWING_DURATION), date.today())
        if days_late >= 1:
            return FINE_PER_DAY * days_late
        else:
            raise ValueError('No fine necessary.')
