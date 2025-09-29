package com.actresssearch.domain.usecase

/**
 * Base contract for suspend use cases.
 */
fun interface SuspendUseCase<in P, out R> {
    suspend operator fun invoke(params: P): R
}
