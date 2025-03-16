/**
 * Copyright 2025 znci
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.znci.rocket.data

import com.dieselpoint.norm.Database
import org.bukkit.plugin.java.JavaPlugin

/**
 * The DataManager object is used to manage the database connection and other data-related tasks
 */
object DataManager {
    private var plugin: JavaPlugin? = null;
    private var db: Database = Database()

    /**
     * Initializes the DataManager
     *
     * @param plugin The plugin to initialize the DataManager with
     */
    fun init(plugin: JavaPlugin) {
        DataManager.plugin = plugin
        val databasePath = plugin.config.getString("database_path")
        if (databasePath.isNullOrEmpty()) {
            throw IllegalArgumentException("Database path not found in config")
        }
        DataManager.plugin!!.logger.info("Database path set to $databasePath")
        try {
            db.setJdbcUrl("jdbc:h2:$databasePath")
            DataManager.plugin!!.logger.info("Database connection initialized")
        } catch (e: Exception) {
            throw IllegalStateException("Failed to initialize database connection", e)
        }
    }

    /**
     * Destroys the active database connection
     */
    fun destroy() {
        db.close()
    }

    /**
     * Gets the active database
     *
     * @return The active database
     */
    fun getDatabase(): Database {
        if (db.connection == null) {
            throw IllegalStateException("Database connection is not initialized")
        }
        return db
    }
}