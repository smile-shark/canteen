package com.smileshark.socket;

import com.smileshark.code.ResultCode;
import com.smileshark.common.CustomerSocketMessage;
import com.smileshark.common.Result;
import com.smileshark.entity.Customer;
import com.smileshark.entity.CustomerOrder;
import com.smileshark.service.CustomerOrderService;
import com.smileshark.utils.InfoThreadLocal;
import com.smileshark.utils.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class OrderServiceController {
    private final TokenUtil tokenUtil;
    private final SimpMessagingTemplate messagingTemplate;
    private final CustomerOrderService customerOrderService;

    @MessageMapping("/order.service")
    public void orderService(@Payload CustomerSocketMessage customerSocketMessage, @Header(required = false) String cuisineId, @Header Integer orderType, @Header(required = false) Boolean isAdd, @Header String diningTableId) {
        System.out.println(isAdd);
        Customer customer = tokenUtil.parseToken(customerSocketMessage.getToken(), Customer.class);
        if (customer == null) {
            // 将身份验证失败的消息发送给对应的人
            messagingTemplate.convertAndSendToUser(
                    customer.getCustomerId(), "/queue/order.add.customer.response", Result.error(ResultCode.TOKEN_ERROR)
            );
            return;
        }
        InfoThreadLocal.setCustomer(customer);
        // 这里获取到的数据将广播给这个桌子上面的所有人
        Result<CustomerOrder> customerOrderResult = customerOrderService.takeOutAndDineInOrder(cuisineId, orderType, isAdd,diningTableId);
        if (diningTableId == null || diningTableId.isEmpty()) {
            messagingTemplate.convertAndSendToUser(customer.getCustomerId(), "/topic/order.add.customer.response", Result.error(ResultCode.PARAM_ERROR));
            return;
        }
        messagingTemplate.convertAndSend("/topic/table/" + diningTableId, customerOrderResult);
        System.out.println(customer.getAccount() + " 添加了菜");
        System.out.println("菜品id:"+cuisineId);
    }

    @MessageMapping("/order.add.customer")
    public void addCustomer(@Payload CustomerSocketMessage customerSocketMessage, SimpMessageHeaderAccessor headerAccessor) {
        Customer customer = tokenUtil.parseToken(customerSocketMessage.getToken(), Customer.class);
        if (customer == null) {
            // 将身份验证失败的消息发送给对应的人
            messagingTemplate.convertAndSendToUser(
                    customer.getCustomerId(), "/queue/order.add.customer.response", Result.error(ResultCode.TOKEN_ERROR)
            );
            return;
        }
        if (customerSocketMessage.getDiningTableId() == null || customerSocketMessage.getDiningTableId().isEmpty()) {
            // 没有传递桌位的信息，返回错误的信息
            messagingTemplate.convertAndSendToUser(customer.getCustomerId(), "/topic/order.add.customer.response", Result.error(ResultCode.PARAM_ERROR));
            return;
        }
        headerAccessor.getSessionAttributes().put("customer", customer.getCustomerId());
        messagingTemplate.convertAndSendToUser(customer.getCustomerId(), "/topic/order.add.customer.response", Result.success());
        System.out.println(customer.getAccount() + " 开始点餐");
        System.out.println("座位:"+customerSocketMessage.getDiningTableId());
        // TODO 这里可能需要改变桌子的状态
    }
}
