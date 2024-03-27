package com.tiodev.vegtummy.RoomDB;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Recipe.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract RecipeDao recipeDao();

    private static volatile AppDatabase INSTANCE; // Singleton instance

    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "recipe.db")
                            // Migration strategies for database version updates can be added here
                            .fallbackToDestructiveMigration() // Use with caution, as this will destroy previous data on version mismatch
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
