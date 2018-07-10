package com.soling.video.ui

import kotlin.properties.Delegates

class User {
    var age: Age  by Delegates.observable(Age(5)) {
        prop, old, new ->
        println("age==>$old -> $new")
    }
    var name: String by Delegates.observable("<no name>") {
        prop, old, new ->
        println("$old -> $new")
    }



}