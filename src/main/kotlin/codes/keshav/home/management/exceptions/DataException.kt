package codes.keshav.home.management.exceptions

class DataException(message: String) : RuntimeException(message)
class UserNotFoundException(message: String) : RuntimeException(message)
class ApartmentCreationException(message: String) : RuntimeException(message)
class DataConversionException(message: String) : RuntimeException(message)
class WebSocketAuthenticationException(message: String) : RuntimeException(message)
class NoApiKeyException(message: String) : RuntimeException(message)
class UserUpdateException(message: String) : RuntimeException(message)
