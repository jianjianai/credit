package cn.jji8.credit;

import cn.jji8.credit.command.completion;
import cn.jji8.credit.command.implement;
import cn.jji8.credit.integral.display;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class main extends JavaPlugin {
    static main main;
    public display display = new display();
    Metrics Metrics;

    /**
     * 插件启动时调用的方法
     * */
    @Override
    public void onEnable() {
        getLogger().info("插件开始加载..");
        Metrics = new Metrics(this,8719);
        main =this;
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(new monitor(),this);//注册监听器
        Bukkit.getPluginCommand("credit").setExecutor(new implement());
        Bukkit.getPluginCommand("credit").setTabCompleter(new completion());
        HologramsAPI.unregisterPlaceholders(this);
        getLogger().info("加载完成");
    }
    /**
     * 获取插件主类
     * */
    public static cn.jji8.credit.main getMain() {
        return main;
    }
    /**
     * 用于获取显示
     * */
    public cn.jji8.credit.integral.display getDisplay() {
        return display;
    }
}
