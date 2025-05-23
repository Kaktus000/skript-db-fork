/*
 * MIT License
 *
 * Copyright (c) 2016 Bryan Terce
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.btk5h.skriptdb;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.registrations.Classes;
import org.bukkit.plugin.java.JavaPlugin;
import com.btk5h.skriptdb.skript.EffExecuteStatement;
import com.zaxxer.hikari.HikariDataSource;
import java.io.IOException;
import java.sql.SQLException;

import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;

/**
 * # skript-db
 *
 * > Sensible SQL support for Skript.
 *
 * @index -1
 */
public final class SkriptDB extends JavaPlugin {

  private static SkriptDB instance;
  private static SkriptAddon addonInstance;

  private static RowSetFactory rowSetFactory;

  public SkriptDB() {
    if (instance == null) {
      instance = this;
    } else {
      throw new IllegalStateException();
    }
  }

  @Override
  public void onEnable() {
    try {
      rowSetFactory = RowSetProvider.newFactory();

      // First register the addon
      addonInstance = Skript.registerAddon(this);

      // IMPORTANT: Register Types class first, it contains our datasource class registration
      try {
        Class.forName("com.btk5h.skriptdb.skript.Types");
      } catch (ClassNotFoundException e) {
        getLogger().severe("Could not register Types class: " + e.getMessage());
      }

      // Then load other classes
      addonInstance.loadClasses("com.btk5h.skriptdb.skript");

      // Don't register datasource class here, it's already done in Types class

    } catch (Exception e) {
      getLogger().severe("Failed to initialize skript-db: " + e.getMessage());
      e.printStackTrace();
      getServer().getPluginManager().disablePlugin(this);
    }
  }

  public static SkriptAddon getAddonInstance() {
    if (addonInstance == null) {
      addonInstance = Skript.registerAddon(getInstance());
    }
    return addonInstance;
  }

  public static SkriptDB getInstance() {
    if (instance == null) {
      throw new IllegalStateException();
    }
    return instance;
  }

  public static RowSetFactory getRowSetFactory() {
    return rowSetFactory;
  }
}
