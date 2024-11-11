package store.service;

import camp.nextstep.edu.missionutils.DateTimes;
import java.util.List;
import store.dto.request.OrderItemInputDto;
import store.dto.response.PromotionResultDto;
import store.model.order.Order;
import store.model.product.ProductManager;

public class OrderService {
    private final ProductManager productManager;

    public OrderService(ProductManager productManager) {
        this.productManager = productManager;
    }

    public Order createOrder(List<OrderItemInputDto> orderItemInputsDto) {
        return new Order(productManager, orderItemInputsDto, DateTimes.now().toLocalDate());
    }

    public List<PromotionResultDto> processPromotions(Order order) {
        return null;
    }
}

