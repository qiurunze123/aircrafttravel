package com.travel.function.zk;

import com.travel.commons.enums.ProductSoutOutMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author 邱润泽 bullock
 */
@Slf4j
public class WatcherApi implements Watcher {

    @Autowired
    private ZkApi zkApi;

    @Override
    public void process(WatchedEvent event) {
        if (event.getType() == Event.EventType.NodeDataChanged) { // zk目录节点数据变化通知事件
            try {
                String path = event.getPath();  //  /product_sold_out/10270
                String soldOutFlag = new String(zkApi.getData(path, null));
                log.info("zookeeper数据节点修改变动,path={},value={}", path, soldOutFlag);
                if ("false".equals(soldOutFlag)) {
                    String productId = path.substring(path.lastIndexOf("/")+1, path.length());
                    ProductSoutOutMap.getProductSoldOutMap().remove(productId);
                }
            } catch (Exception e) {
                log.error("zookeeper数据节点修改回调事件异常", e);
            }
        }
    }
}
