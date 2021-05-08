package com.patagonian.lyrics.domain.exception

/**
 * Business exceptions
 */
sealed class DomainException : Exception() {

    /**
     * Validation exceptions
     */
    sealed class ValidationException : DomainException() {
        class BlankField(val field: String) : ValidationException()
    }

    /**
     * Transaction exceptions
     */
    sealed class TransactionException : DomainException() {
        object RecordNotFound : TransactionException()
    }

    /**
     * Network exceptions
     */
    sealed class NetworkException : DomainException() {
        object NetworkNotAvailable : NetworkException()
    }
}
