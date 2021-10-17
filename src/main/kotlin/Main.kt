import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

// https://www.youtube.com/watch?v=C38lG2wraoo&list=PLlxmoA0rQ-Lzyprk1wxs4CT15hOqvW0oC&index=1

fun main() {

    println("Execution Start: ${Thread.currentThread().name} Thread")

    /**
     * Call suspend function inside a runBlocking{}
     * runBlocking{} is current thread(here main) coroutine scope
     */
    runBlocking {
        myGlobalLaunch()
        myLocalLaunch()
    }

    /**
     * Coroutines Builders:
     * launch, async, runBlocking
     */

    myAsync()
   // myTimeout()
    myTimeoutOrNull()

    mySameTimeExecution()
    myLazyExecution()
    corDispatchers()

    println("Execution End: ${Thread.currentThread().name} Thread")

}

suspend fun myDelay(millis: Long) {
    delay(millis)
//    yield() // for small delay
}

suspend fun myGlobalLaunch() {
    /**
     * GlobalScope.launch{} live until application is alive doesn't depend on activity life
     * Use when need background task like: File Download/Play Music
     * Need more memory. Don't use if not necessary
     */
    GlobalScope.launch {
        println("Execution 1 ${Thread.currentThread().name} Thread")
        myDelay(1000)
        println("Execution 2 ${Thread.currentThread().name} Thread")
    }
}

suspend fun myLocalLaunch() {
    coroutineScope {
        /**
         * launch{} live until activity is live (local scope)
         * Use when need background task like: Data computation/Api Call etc. when no need this object after destroying the activity UI
         * launch{} inherits immediate parent coroutine if parent runs on Main Thread this will run on Main Thread
         */
        val job: Job = launch {
            println("Execution 3 ${Thread.currentThread().name} Thread")
            try {
                myDelay(1000)
            } catch (e: CancellationException) {
                e.printStackTrace()
            }
            println("Execution 4 ${Thread.currentThread().name} Thread")
        }
        /**
         * If cancel the coroutine use try catch for
         * CancellationException on delay()/yield() function
         */
        job.cancelAndJoin() // To cancel coroutine.

    }
}

fun myAsync() = runBlocking {

    /**
     * async for long time operation (asynchronous operation)
     * await for calling and getting output of async
     * difference with launch is async can return data
     */
    val jobDeferred: Deferred<String> = async {
        println("Execution 5 ${Thread.currentThread().name} Thread")
        "Wow Working"
    }
    val myMsg: String = jobDeferred.await()

    println(myMsg)
}

fun myTimeout() = runBlocking {
    withTimeout(3000) {
        try {
            for (i in 0..500) {
                print("$i ")
                // can check it is canceled or not using this
//                if (isActive) {
//                    return@withTimeout
//                }
                delay(500)
            }
        } catch (e: TimeoutCancellationException) {
            println("Exception happens ${e.message}")
        }
    }

}

fun myTimeoutOrNull() = runBlocking {
    val r: String? = withTimeoutOrNull(3000) {
        for (i in 500..1500) {
            print("$i ")
            delay(500)
        }
        "Timeout happens"
    }
    println(r)
}

fun mySameTimeExecution() = runBlocking {
    val mTime = measureTimeMillis {
        val msgOne: Deferred<String> = async { getMsgOne() }
        val msgTwo: Deferred<String> = async { getMsgTwo() }
        val result = "${msgOne.await()} ${msgTwo.await()}"
        println(result)
    }
    /**
     * If you don't use async and await the execution time will be 2000 millis (Sequential execution)
     * if use 1000 millis as both runs simultaneously (Concurrent execution)
     */
    println("Execution Time: $mTime")
}

suspend fun getMsgOne(): String {
    delay(1000L)
    return "Message One"
}

suspend fun getMsgTwo(): String {
    delay(1000L)
    return "Message Two"
}

fun myLazyExecution() = runBlocking {
    /**
     * Will not execute if variable are unused
     * Will execute when await() function calls
     */
    println("Start LAZY:")
    val msgOne: Deferred<String> = async(start = CoroutineStart.LAZY) { getMsgOne() }
    val msgTwo: Deferred<String> = async(start = CoroutineStart.LAZY) { getMsgTwo() }
    val result = "${msgOne.await()} ${msgTwo.await()}"
    println(result)
}

fun corDispatchers() = runBlocking {

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
//    launch(Dispatchers.Main) {
//        // For Main thread with UI object. Use on Android UI, JavaFX, Swing
//    }

}