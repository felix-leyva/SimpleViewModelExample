package de.felixlf.simpleviewmodelexample.uimodel

import kotlinx.coroutines.flow.SharingStarted
import kotlin.coroutines.CoroutineContext
import kotlin.jvm.JvmInline

@JvmInline
value class UIDispatcher(val context: CoroutineContext)

@JvmInline
value class UIStateSharing(val started: SharingStarted)
