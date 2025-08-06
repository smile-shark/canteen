package com.smileshark.service.imp;

import com.smileshark.entity.Printer;
import com.smileshark.mapper.PrinterMapper;
import com.smileshark.service.PrinterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 打印机 服务实现类
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月06日
 */
@Service
public class PrinterServiceImp extends ServiceImpl<PrinterMapper, Printer> implements PrinterService {

}
