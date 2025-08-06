package com.smileshark.service.imp;

import com.smileshark.entity.Receipt;
import com.smileshark.mapper.ReceiptMapper;
import com.smileshark.service.ReceiptService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 小票的样式管理 服务实现类
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月06日
 */
@Service
public class ReceiptServiceImp extends ServiceImpl<ReceiptMapper, Receipt> implements ReceiptService {

}
