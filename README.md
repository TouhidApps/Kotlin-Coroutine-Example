# Kotlin Coroutine Example
Kotlin Coroutine Example


Based on this playlist:


https://www.youtube.com/watch?v=C38lG2wraoo&list=PLlxmoA0rQ-Lzyprk1wxs4CT15hOqvW0oC&index=1



### Key Items:


runBlocking{ }


suspend


GlobalScope.launch { }


coroutineScope { }


launch{} live until activity is live (local scope)


job.cancelAndJoin()


async { }


await()


withTimeout(--) { }


withTimeoutOrNull(--) { }


isActive


delay(--)


async(start = CoroutineStart.LAZY) { mySuspendFunction() }


### Dispatchers:


    launch {
        // no param is same as Default dispatchers below
    }
    launch(Dispatchers.Default) {
        // Shared thread execution, code can run parent thread or any other thread
    }
    launch(Dispatchers.Unconfined) {
        // Code can run from any thread (not confined)
    }
    launch(Dispatchers.IO) {
        // For Network call, File read/write or long operation
    }
    launch(Dispatchers.Main) {
        // For Main thread with UI object. Use on Android UI, JavaFX, Swing
    }


-- DONE --
