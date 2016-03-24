package cn.imrhj.pushmsg.DataLoader;

import java.util.List;

import cn.imrhj.pushmsg.Utils.ListData;

/**
 * Created by rhj on 16/3/17.
 */
public interface DataCache {
    public List<ListData> get();
    public void put(List<ListData> datas);
}
