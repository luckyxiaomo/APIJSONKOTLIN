package com.suxiaomo

import com.suxiaomo.kotlin.util.DateTimeTypeAdapter
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.auth.*
import io.ktor.gson.*
import io.ktor.features.*
import org.jetbrains.exposed.sql.Database
import org.joda.time.DateTime
import org.slf4j.event.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    connectDatabase()

    install(Authentication) {
    }

    install(ContentNegotiation) {
        gson {
            registerTypeAdapter(DateTime::class.java, DateTimeTypeAdapter)
            serializeNulls() //  序列化null
            // 设置日期时间格式，另有2个重载方法
            // 在序列化和反序化时均生效
            setDateFormat("yyyy-MM-dd HH:mm:ss")
            setPrettyPrinting()
            disableHtmlEscaping()
        }
    }

    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }

    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header(HttpHeaders.Authorization)
        header("MyCustomHeader")
        allowCredentials = true
        anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
    }

    install(DefaultHeaders) {
        header("X-Engine", "Ktor") // will send this header with each response
    }

    install(Routing) {
        route("v1") {
            get {
                call.respond(mapOf("hello" to "world"))
            }

            post {

            }

            options {

            }

            post("upload") {

            }

            post("download") {

            }
        }
    }
}

fun connectDatabase() {
    val config = HikariConfig("resources/hikari.properties")
    val ds = HikariDataSource(config)
    Database.connect(ds)

//    var theTables = arrayOf(
//
//    )
//
//    transaction {
//        //  CREATE TABLE IF NOT EXISTS
//        SchemaUtils.create(*theTables)
////
////        if (AdministratorServer.getFileKey() == null) {
////            // 初始化权限表
////            RoleServer.createOne("admin", "超级管理员")
////
////            // 初始化超级管理员账户
////            AdministratorServer.createOne(1, "", "admin-w", DigestUtils.md5Hex("654321"), "")
////        }
//    }
}

