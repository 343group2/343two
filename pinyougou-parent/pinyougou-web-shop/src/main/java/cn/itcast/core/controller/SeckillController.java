package cn.itcast.core.controller;

import cn.itcast.core.service.SeckillService;
import com.alibaba.dubbo.config.annotation.Reference;
import entity.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vo.GoodsVo;

@RestController
@RequestMapping("/seckill")
public class SeckillController {
    @Reference
    private SeckillService seckillService;
    @RequestMapping("/seckillAdd")
    public Result seckillAdd(@RequestBody GoodsVo goodsVo){
        try {
            seckillService.seckillAdd(goodsVo);
            return new Result(true,"秒杀商品添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,"秒杀商品添加失败");
        }
    }
}
