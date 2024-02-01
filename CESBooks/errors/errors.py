
class Error(Exception):
    """Base class for other exceptions"""
    pass


class IncompleteBookError(Error):
    pass


class NotValidISBNError(Error):
    pass
