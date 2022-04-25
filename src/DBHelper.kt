import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class DBHelper(
    val dbName: String,
    val address: String = "localhost",
    val port: Int = 3306,
    val user: String = "root",
    val password: String = "root"
) {
    private var connection: Connection? = null
    init{
        try {
            connection = DriverManager.getConnection(
                "jdbc:mysql://$address:$port/$dbName?serverTimezone=UTC",
                user,
                password
            )
            /////////
            //Демонстрационная часть!!!!!
            val s = connection?.createStatement()
            val sql = "SELECT * FROM `groups`"
            val rs = s?.executeQuery(sql)
            while (rs?.next() == true){
                var data = rs.getString("col_name")
            }
            rs?.close()
            s?.close()
            val sql_create = "create table if not exists `test` (\n" +
                    "    `id` int primary key auto_increment,\n" +
                    "    `text_field` varchar(50) not null,\n" +
                    "    `int_field` int default 0\n" +
                    ")"
            s?.execute(sql_create)
            s?.execute("delete from `test`")
            /*(1..10).forEach {
                val sql_insert = "insert into `test` (text_field, int_field) values ('Текстовое поле', $it)"
                s?.execute(sql_insert)
            }*/
            (1..10).forEach {
                val ps = connection?.prepareStatement("insert into `test` (text_field, int_field) values (?, ?)")
                ps?.setString(1, "Значение текстового поля")
                ps?.setInt(2, it)
                val rows = ps?.executeUpdate()
                println(rows)
                ps?.close()
            }

        } catch (e: SQLException){
            println("Ошибка создания таблицы:\n${e.toString()}")
        }
    }
}