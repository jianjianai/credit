package cn.jji8.credit;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * 监听器类，用于监听事件
 * */
public class monitor implements Listener {
    /**
     * 玩家进入服务器时触发
     * */
    @EventHandler
    public void Listener(PlayerJoinEvent a){
        Thread T = new Thread(() -> {
            main.getMain().getDisplay().add(a.getPlayer());
        });
        T.setName("加载玩家配置");
        T.start();
    }
    /**
     * 玩家离开服务器时触发
     * */
    @EventHandler
    public void Listener(PlayerQuitEvent a){
        main.getMain().getDisplay().Remove(a.getPlayer());
    }
    /**
     * 玩家移动时触发
     * */
    @EventHandler
    public void Listener(PlayerMoveEvent a){
        main.getMain().getDisplay().move(a.getPlayer());
    }
}
