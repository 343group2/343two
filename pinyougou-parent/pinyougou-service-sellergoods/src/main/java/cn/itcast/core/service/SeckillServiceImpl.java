package cn.itcast.core.service;

import cn.itcast.common.utils.IdWorker;
import cn.itcast.core.dao.seckill.SeckillGoodsDao;
import cn.itcast.core.dao.seckill.SeckillOrderDao;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.pojo.seckill.SeckillOrderQuery;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import vo.GoodsVo;

import java.util.Date;

@Service
public class SeckillServiceImpl implements SeckillService {
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private SeckillOrderDao seckillOrderDao;
    @Autowired
    private SeckillGoodsDao seckillGoodsDao;
    @Override
    public void seckillAdd(GoodsVo goodsVo) {
        //创建一个秒杀商品对象
        SeckillGoods seckillGoods = new SeckillGoods();
        //生成秒杀商品id
        long id = idWorker.nextId();
        //设置id
        seckillGoods.setId(id);
        //设置goosID
        seckillGoods.setGoodsId(goodsVo.getGoods().getId());
        //判断是否启用规格
            //设置标题
        for (Item item : goodsVo.getItemList()) {
            //设置标题title
            seckillGoods.setTitle(item.getTitle());
            //设置图片
            seckillGoods.setSmallPic(item.getImage());
            //设置价格
            if ("1".equals(goodsVo.getGoods().getIsEnableSpec())) {
                seckillGoods.setPrice(item.getPrice());
                //设置秒杀数量
            }else {
                seckillGoods.setPrice(goodsVo.getGoods().getPrice());
            }
        }
        //设置秒杀价格
        seckillGoods.setCostPrice(goodsVo.getCostPrice());
        //设置商家ID
        seckillGoods.setSellerId(goodsVo.getGoods().getSellerId());
        //设置创建时间
        seckillGoods.setCreateTime(new Date());
        //设置状态
        seckillGoods.setStatus("0");
        //设置开始时间
        seckillGoods.setStartTime(goodsVo.getStartTime());
        //设置结束时间
        seckillGoods.setEndTime(goodsVo.getEndTime());
        //设置秒杀数量
        seckillGoods.setNum(goodsVo.getNum());
        //设置库存剩余数量
        //统计订单数量
        SeckillOrderQuery seckillOrderQuery = new SeckillOrderQuery();
        seckillOrderQuery.createCriteria().andSeckillIdEqualTo(id);
        seckillGoods.setStockCount(goodsVo.getNum()-seckillOrderDao.countByExample(seckillOrderQuery));
        //保存秒杀商品
        seckillGoodsDao.insertSelective(seckillGoods);
    }
}
