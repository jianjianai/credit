package cn.jji8.credit.integral;

import cn.jji8.credit.Metrics;
import cn.jji8.credit.main;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 负责显示玩家积分
 * */
public class display {
    HashMap<Player,people> 表 = new HashMap<Player,people>();
    /**
     * 用于添加需要显示的玩家
     * */
    public void add(Player player) {
        表.put(player,new people(player));
    }
    /**
     * 删除需要显示的玩家
     * */
    public void Remove(Player player) {
        people people = 表.get(player);
        if(people==null){
            main.getMain().getLogger().info("显示列表中没有这个玩家"+player.getName()+"，无法删除，可能是bug导致，建议反馈作者");
            return;
        }
        people.delete();
        表.remove(player);
    }
    /**
     * 玩家移动时，调用使积分跟着玩家
     * */
    public void move(Player player) {
        people people = 表.get(player);
        if(people==null){
            表.put(player,new people(player));
            main.getMain().getLogger().info("显示列表中没有这个玩家"+player.getName()+"，可能是bug导致，建议反馈作者");
            return;
        }
        people.move();
    }
    /**
     * 评分方法，管理给玩家评分
     * */
    public boolean score(Player 执行者,Player 玩家,int 分数,String 原因){
        people people = 表.get(玩家);
        if(people==null){
            main.getMain().getLogger().info("显示列表中没有这个玩家"+玩家.getName()+"，可能是bug导致，建议反馈作者");
            return false;
        }
        return people.score(执行者,分数,原因);
    }
    /**
     * 评分方法，玩家个玩家评价
     * */
    public boolean evaluate(Player 执行者,Player 玩家,int 分数){
        people people = 表.get(玩家);
        if(people==null){
            main.getMain().getLogger().info("显示列表中没有这个玩家"+玩家.getName()+"，可能是bug导致，建议反馈作者");
            return false;
        }
        return people.evaluate(执行者,分数);
    }
    /**
     * 获取记录
     * */
    public List<String> get记录(Player 玩家) {
        people people = 表.get(玩家);
        if(people==null){
            main.getMain().getLogger().info("显示列表中没有这个玩家"+玩家.getName()+"，可能是bug导致，建议反馈作者");
            return new ArrayList<>();
        }
        return people.get记录();
    }
}
