package cn.jji8.credit.integral;

import cn.jji8.credit.main;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 负责处理玩家积分
 * */
public class people {
    Player Player;
    int 积分 ;
    YamlConfiguration wenjian;
    File 路径;
    Hologram Hologram;
    double 上移位置 = main.getMain().getConfig().getDouble("上移位置");
    double 最小平均分 = main.getMain().getConfig().getDouble("最小平均分");
    List<String> 文本 = main.getMain().getConfig().getStringList("头顶显示文本");
    List<Map<?, ?>> 信用显示 = main.getMain().getConfig().getMapList("信用等级名称");
    String 时间格式 = main.getMain().getConfig().getString("时间格式");
    String 记录格式 = main.getMain().getConfig().getString("记录格式");
    String 踢出消息 = main.getMain().getConfig().getString("踢出消息");
    boolean 显示文本 = main.getMain().getConfig().getBoolean("显示文本");
    String 查询显示文本 = main.getMain().getConfig().getString("查询显示文本");
    int 保留小数位数 = main.getMain().getConfig().getInt("保留小数位数");
    int 最小数量 = main.getMain().getConfig().getInt("最小数量");
    int 封禁积分 = main.getMain().getConfig().getInt("封禁积分");
    List<String> 记录;
    List<Map<?, ?>> 玩家评分;
    Player 玩家;

    /**
     * 构造方法一个，根据玩家来加载配置文件
     * */
    people(Player a){
        玩家 = a;
        if(文本==null){
            main.getMain().getLogger().warning("配置文件中没有“头顶显示文本”无法加载");
            return;
        }
        Player = a;
        if(main.getMain().getConfig().getBoolean("跨服模式")){
            路径 = new File(main.getMain().getConfig().getString("工作路径")+"/integral/"+Player.getName());
        }else {
            路径 = new File(main.getMain().getDataFolder(),"integral/"+Player.getName());
        }
        wenjian = YamlConfiguration.loadConfiguration(路径);
        if(wenjian.contains("积分")){
            积分 = wenjian.getInt("积分");
        }else {
            积分 = main.getMain().getConfig().getInt("基础积分");
        }
        记录 = wenjian.getStringList("记录");
        玩家评分 = wenjian.getMapList("玩家评分");
        if(显示文本){
            BukkitRunnable BukkitRunnable = new BukkitRunnable() {
                @Override
                public void run() {
                    if(积分<封禁积分){
                        a.kickPlayer(踢出消息);
                        return;
                    }
                    Hologram = 	HologramsAPI.createHologram(main.getMain(),a.getLocation().add(0,上移位置,0));
                    显示(文本);
                }
            };
            BukkitRunnable.runTask(main.getMain());
        }
    }
    /**
     * 即将被删除时被调用的方法，删掉全息显示
     * */
    public void delete() {
        if(Hologram==null){
            return;
        }
        Hologram.delete();
    }
    /**
     * 移动这个全息表，使他跟着玩家
     * */
    public void move() {
        if(显示文本){
            if(Hologram==null){
                return;
            }
            Hologram.teleport(Player.getLocation().add(0,上移位置,0));
        }
    }
    /**
     * 将全息显示的内容改成文本
     * */
    void 显示(List<String> 文本){
        if(Hologram==null){
            return;
        }
        List<String> List = 替换(文本);
        Hologram.clearLines();
        for(String A:List){
            Hologram.appendTextLine(A);
        }
    }

    /**
     * 将list里的文本全部用变量替换一遍
     * */
    int 评价人数 = 0;
    List<String> 替换(List<String> 文本){
        ArrayList<String>  ArrayList = new ArrayList<String>(文本);
        String 信誉显示文本 = get信用等级名称();

        for(int i = 0;i<ArrayList.size();i++){
            String 文字 = ArrayList.get(i)
                    .replaceAll("%积分%",Integer.toString(积分))
                    .replaceAll("%信誉等级%",信誉显示文本)
                    .replaceAll("%玩家评分%",get玩家平均分TXT())
                    .replaceAll("%评价人数%",Integer.toString(评价人数/2));
            ArrayList.set(i,文字);
        }
        return ArrayList;
    }
    /**
     * 用来处理保留小数的显示字符串
     * */
    public String XianShiZiFu(double qian){
        String 价格字符 =  String.valueOf(qian);
        int 小数点位置 = 价格字符.indexOf(".");
        String 价格字符舍;
        if(价格字符.length()>小数点位置+保留小数位数+1){
            价格字符舍 = 价格字符.substring(0,小数点位置+保留小数位数+1);
        }else {
            价格字符舍 = 价格字符;
        }
        double 小数 = 1;
        for(int i = 0;i<保留小数位数;i++){
            小数 /= 10;
        }
        if(qian<=小数){
            StringBuffer  S = new StringBuffer("0.");
            for(int i = 1;i<保留小数位数;i++){
                S.append("0");
            }
            S.append("1");
            价格字符舍 = S.toString();
        }
        return 价格字符舍;
    }
    /**
     * 获取文本形式得玩家平均分
     * */
    String get玩家平均分TXT(){
        double 评价分 = get玩家平均分();
        if(评价分<0){
            return main.getMain().getConfig().getString("无评分显示字符");
        }
        return XianShiZiFu(评价分);
    }
    /**
     * 获取玩家平均分
     * */
    double get玩家平均分(){
        int 数量 = 0;
        double 总分 = 0;
        for(Map A:玩家评分){
            数量++;
            总分 += (int) A.get("评分");
        }
        评价人数 = 数量;
        if(数量<最小数量){
            return -10;
        }
        double 平均分 = 总分/数量;
        if(平均分<最小平均分){
            玩家.kickPlayer(踢出消息);
        }
        return 平均分;
    }
    /**
     * 获取应该显示得信用等级
     * */
    String get信用等级名称(){
        for(Map map:信用显示){
            int 最低积分 = (int) map.get("最低积分");
            int 最高积分 = (int) map.get("最高积分");
            if(积分>=最低积分&积分<=最高积分){
                return (String) map.get("名称");
            }
        }
        return "此积分配置文件中无配置";
    }

    /**
     * 给玩家评分
     * */
    public boolean score(Player 执行者,int 分数,String 原因) {
        积分+=分数;
        显示(文本);
        Thread T = new Thread(()->{
            SimpleDateFormat 时间 = new SimpleDateFormat(时间格式);
            String 记录 = 记录格式
                    .replaceAll("%时间%",时间.format(new Double(System.currentTimeMillis())))
                    .replaceAll("%执行者%",执行者.getName())
                    .replaceAll("%积分%",Integer.toString(分数))
                    .replaceAll("%原因%",原因);

            this.记录.add(记录);
            wenjian.set("记录",this.记录);
            wenjian.set("积分",积分);
            try {
                wenjian.save(路径);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        T.setName("保存线程");
        T.start();

        if(积分<封禁积分){
            玩家.kickPlayer(main.getMain().getConfig().getString("踢出消息"));
        }
        return true;
    }

    /**
     * 给玩家评价
     * */
    public boolean evaluate(Player 执行者,int 分数) {
        Map a = 查找玩家map(执行者.getName());
        if(a==null){
            a = new HashMap();
            a.put("玩家",执行者.getName());
            a.put("评分",分数);
            玩家评分.add(a);
            玩家.sendMessage("评分成功");
            玩家评分.add(a);
        }else {
            a.put("评分",分数);
            玩家.sendMessage("修改评分成功");
        }
        wenjian.set("玩家评分",玩家评分);
        显示(文本);
        Thread T = new Thread(()->{
            try {
                wenjian.save(路径);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        T.setName("保存线程");
        T.start();
        return true;
    }
    /**
     * 在玩家评分中使用玩家名字查找map，没找到返回null
     * */
    public Map 查找玩家map(String 玩家名字){
        for(Map a:玩家评分){
            if(玩家名字.equals(a.get("玩家"))){
                return a;
            }
        }
        return null;
    }

    /**
     * 获取记录
     * */
    public List<String> get记录() {
        ArrayList ArrayList = new ArrayList(记录);
        ArrayList.add(查询显示文本.replaceAll("%积分%",Integer.toString(积分)).replaceAll("%信誉等级%",get信用等级名称()));
        return ArrayList;
    }
}
