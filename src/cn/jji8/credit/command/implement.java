package cn.jji8.credit.command;


import cn.jji8.credit.integral.display;
import cn.jji8.credit.main;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.mysql.jdbc.Field;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.text.MessageFormat;
import java.util.List;


/**
 * 一个命令执行器,专用于执行命令
 * */
public class implement implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] 参数){
        if(!(commandSender instanceof Player)){
            commandSender.sendMessage("此命令必须玩家执行");
            return true;
        }
        Player 玩家 = (Player) commandSender;
        if(参数.length<1){
            help(commandSender);
            return true;
        }
        switch (参数[0]){
            case "评分":
            case "score":{
                if(!commandSender.hasPermission("credit.score")){
                    commandSender.sendMessage("你没有权限执行此命令");
                    return true;
                }
                if(参数.length<4){
                    commandSender.sendMessage("/credit score 玩家 分数 原因      //给玩家评分");
                    return true;
                }
                Player 评分玩家 = org.bukkit.Bukkit.getPlayer(参数[1]);
                if(评分玩家==null){
                    commandSender.sendMessage(参数[1]+"不在线");
                    return true;
                }
                int 分数;
                try {
                    分数 = Integer.parseInt(参数[2]);
                }catch (NumberFormatException a){
                    commandSender.sendMessage(参数[2]+"不是一个有效数字");
                    return true;
                }
                if(分数>0){
                    if(!commandSender.hasPermission("credit.score.increase")){
                        commandSender.sendMessage("你没有权限给玩家加分，需要：credit.score.increase 权限");
                        return true;
                    }
                }
                if(分数<0){
                    if(!commandSender.hasPermission("credit.score.reduce")){
                        commandSender.sendMessage("你没有权限给玩家扣分，需要：credit.score.reduce 权限");
                        return true;
                    }
                }
                if(main.getMain().getDisplay().score(玩家,评分玩家,分数,参数[3])){
                    commandSender.sendMessage("评分成功");
                }else {
                    commandSender.sendMessage("错误？");
                }
                break;
            }
            case "查询":
            case "query":{
                if(!commandSender.hasPermission("credit.query")){
                    commandSender.sendMessage("你没有权限执行此命令");
                    return true;
                }
                if(参数.length<2){
                    commandSender.sendMessage("/credit query 玩家        //查询玩家评分记录");
                    return true;
                }
                Player 查询玩家 = org.bukkit.Bukkit.getPlayer(参数[1]);
                if(查询玩家==null){
                    commandSender.sendMessage(参数[1]+"不在线");
                    return true;
                }
                List<String> 记录 = main.getMain().getDisplay().get记录(查询玩家);
                commandSender.sendMessage(main.yamlLang.getString("Query.Prefix")+
                        main.yamlLang.getString("Query.Title")+
                        main.yamlLang.getString("Query.Suffix"));
                for(String a:记录){
                    commandSender.sendMessage(main.yamlLang.getString("Query.ContentColor").replace("&","§")+a);
                }
                commandSender.sendMessage(main.yamlLang.getString("Query.Prefix")+
                        main.yamlLang.getString("Query.Title")+
                        main.yamlLang.getString("Query.Suffix"));
                break;
            }
            case "评价":
            case "evaluate":{
                if(!commandSender.hasPermission("credit.evaluate")){
                    commandSender.sendMessage("你没有权限执行此命令");
                    return true;
                }
                if(参数.length<3){
                    commandSender.sendMessage("/credit evaluate 玩家 分数      //用于评价玩家");
                    return true;
                }
                Player 评分玩家 = org.bukkit.Bukkit.getPlayer(参数[1]);
                if(评分玩家==null){
                    String format = MessageFormat.format(main.yamlLang.getString("Evaluate.NoOnline"), 参数[1]);
                    commandSender.sendMessage(format);
                    return true;
                }
                int 分数;
                try {
                    分数 = Integer.parseInt(参数[2]);
                }catch (NumberFormatException a){
                    String format = MessageFormat.format(main.yamlLang.getString("Evaluate.ErrorMath"), 参数[2]);
                    commandSender.sendMessage(format);
                    return true;
                }
                if(分数<0|分数>10){
                    commandSender.sendMessage(main.yamlLang.getString("Evaluate.MathNo1_10"));
                    return true;
                }
                if(main.getMain().getDisplay().evaluate(玩家,评分玩家,分数)){
                    commandSender.sendMessage(main.yamlLang.getString("Evaluate.EvaOk"));
                }else {
                    commandSender.sendMessage(main.yamlLang.getString("Evaluate.EvaError"));
                }
                break;
            }

            case "重载":
            case "reload":{
                if(!commandSender.hasPermission("credit.reload")){
                    commandSender.sendMessage("你没有权限执行此命令");
                    return true;
                }
                main.getMain().reloadConfig();
                main.getMain().display = new display();
                HologramsAPI.unregisterPlaceholders(main.getMain());
                File fileLang = new File(main.getMain().getDataFolder(), "lang.yml");
                if (!fileLang.exists()){
                    main.getMain().saveResource("lang.yml",false);
                }
                main.yamlLang = YamlConfiguration.loadConfiguration(fileLang);
                commandSender.sendMessage("插件重新加载完成");
                break;
            }

            default:{
                help(commandSender);
                break;
            }
        }
        return true;
    }

    void help(CommandSender commandSender){
        if(commandSender.hasPermission("credit.score")){
            commandSender.sendMessage("/credit 评分      //给玩家评分");
            commandSender.sendMessage("/credit score      //给玩家评分");
        }
        if(commandSender.hasPermission("credit.query")){
            commandSender.sendMessage("/credit 查询      //查询玩家评分记录");
            commandSender.sendMessage("/credit query      //查询玩家评分记录");
        }
        if(commandSender.hasPermission("credit.evaluate")){
            commandSender.sendMessage("/credit 评价      //用于评价玩家");
            commandSender.sendMessage("/credit evaluate      //用于评价玩家");
        }
        if(commandSender.hasPermission("credit.reload")){
            commandSender.sendMessage("/credit 重载      //重新加载配置文件，建议重启服务器");
            commandSender.sendMessage("/credit reload      //重新加载配置文件，建议重启服务器");
        }
    }
}