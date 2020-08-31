package cn.jji8.credit.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

/**
 * 一个命令补全器,转用于命令补全
 * */
public class completion implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if(strings.length==1){
            ArrayList<String> ArrayList = new ArrayList<String>();
            if(commandSender.hasPermission("credit.buckle")){
                ArrayList.add("评分");
                ArrayList.add("score");
            }
            if(commandSender.hasPermission("credit.query")){
                ArrayList.add("查询");
                ArrayList.add("query");
            }
            if(commandSender.hasPermission("credit.evaluate")){
                ArrayList.add("评价");
                ArrayList.add("evaluate");
            }
            if(commandSender.hasPermission("credit.reload")){
                ArrayList.add("reload");
                ArrayList.add("重载");
            }
            return ArrayList;
        }
        return null;
    }
}
