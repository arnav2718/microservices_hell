package com.example.ecomm_orderservice.services.cart;

import com.example.ecomm_orderservice.dto.AddProductInCartDto;
import com.example.ecomm_orderservice.dto.CartItemsDto;
import com.example.ecomm_orderservice.dto.OrderDto;
import com.example.ecomm_orderservice.dto.PlaceOrderDto;
import com.example.ecomm_orderservice.entity.CartItems;
import com.example.ecomm_orderservice.entity.Order;
import com.example.ecomm_orderservice.entity.Product;
import com.example.ecomm_orderservice.entity.User;
import com.example.ecomm_orderservice.enums.OrderStatus;
import com.example.ecomm_orderservice.repository.CartItemsRepository;
import com.example.ecomm_orderservice.repository.OrderRepository;
import com.example.ecomm_orderservice.repository.ProductRepository;
import com.example.ecomm_orderservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartItemsRepository cartItemsRepository;

    @Autowired
    private ProductRepository productRepository;

//    public ResponseEntity<?> addProductToCart (AddProductInCartDto addProductInCartDto){
//        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(addProductInCartDto.getUserId(), OrderStatus.Pending);
//        Optional<CartItems> optionalCartItems = cartItemsRepository.findByProductIdAndOrderIdAndUserId
//        (addProductInCartDto.getProductId(), activeOrder.getId(), addProductInCartDto.getUserId());
//
//        if(optionalCartItems.isPresent()){
//            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
//        } else {
//            Optional<Product> optionalProduct = productRepository.findById(addProductInCartDto.getProductId());
//            Optional<User> optionalUser = userRepository.findById(addProductInCartDto.getProductId());
//
//            if(optionalProduct.isPresent() && optionalUser.isPresent()){
//                CartItems cart = new CartItems();
//                cart.setProduct(optionalProduct.get());
//                cart.setPrice(optionalProduct.get().getPrice());
//                cart.setQuantity(1L);
//                cart.setUser(optionalUser.get());
//                cart.setOrder(activeOrder);
//
//                CartItems updatedcart = cartItemsRepository.save(cart);
//
//                activeOrder.setTotalAmount(activeOrder.getTotalAmount() + cart.getPrice());
//                activeOrder.setAmount(activeOrder.getAmount() + cart.getPrice());
//                activeOrder.getCartItems().add(cart);
//
//                orderRepository.save(activeOrder);
//
//                return ResponseEntity.status(HttpStatus.CREATED).body(cart);
//            } else {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User or product not found");
//            }
//        }
//    }
public ResponseEntity<?> addProductToCart(AddProductInCartDto addProductInCartDto) {
    Order activeOrder = orderRepository.findByUserIdAndOrderStatus(addProductInCartDto.getUserId(), OrderStatus.Pending);

    // Check if the active order is null (user doesn't have a pending order)
    if (activeOrder == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Active order not found.");
    }

    // Check if the product already exists in the cart
    Optional<CartItems> optionalCartItems = cartItemsRepository.findByProductIdAndOrderIdAndUserId
            (addProductInCartDto.getProductId(), activeOrder.getId(), addProductInCartDto.getUserId());

    // If the item exists, just update the quantity
    if (optionalCartItems.isPresent()) {
        CartItems existingCartItem = optionalCartItems.get();
        existingCartItem.setQuantity(existingCartItem.getQuantity() + 1); // Increment quantity by 1
        cartItemsRepository.save(existingCartItem);

        // Update the order total amounts
        activeOrder.setTotalAmount(activeOrder.getTotalAmount() + existingCartItem.getPrice());
        activeOrder.setAmount(activeOrder.getAmount() + existingCartItem.getPrice());
        orderRepository.save(activeOrder);

        return ResponseEntity.status(HttpStatus.OK).body(existingCartItem); // Return the updated cart item
    } else {
        // Otherwise, create a new cart item
        Optional<Product> optionalProduct = productRepository.findById(addProductInCartDto.getProductId());
        Optional<User> optionalUser = userRepository.findById(addProductInCartDto.getUserId()); // Fixed to look up userId

        if (optionalProduct.isPresent() && optionalUser.isPresent()) {
            CartItems cart = new CartItems();
            cart.setProduct(optionalProduct.get());
            cart.setPrice(optionalProduct.get().getPrice());
            cart.setQuantity(1L); // Initial quantity is 1
            cart.setUser(optionalUser.get());
            cart.setOrder(activeOrder);

            CartItems savedCart = cartItemsRepository.save(cart);

            // Update the order total amounts
            activeOrder.setTotalAmount(activeOrder.getTotalAmount() + cart.getPrice());
            activeOrder.setAmount(activeOrder.getAmount() + cart.getPrice());
            activeOrder.getCartItems().add(cart);

            orderRepository.save(activeOrder);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedCart);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User or product not found");
        }
    }
}

    public OrderDto getCartByUserId (Long userId) {
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(userId, OrderStatus.Pending);

        List<CartItemsDto> cartItemsDtoList = activeOrder.getCartItems().stream().map(CartItems::getCartDto).collect(Collectors.toList());
        OrderDto orderDto = new OrderDto();
        orderDto.setAmount(activeOrder.getAmount());
        orderDto.setId(activeOrder.getId());
        orderDto.setOrderStatus(activeOrder.getOrderStatus());
        orderDto.setTotalAmount(activeOrder.getTotalAmount());

        orderDto.setCartItems(cartItemsDtoList);

        return orderDto;
    }

    public OrderDto increaseProductQuantity (AddProductInCartDto addProductInCartDto) {
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus (addProductInCartDto.getUserId(), OrderStatus. Pending);
        Optional <Product> optionalProduct = productRepository.findById(addProductInCartDto.getProductId());

        Optional <CartItems> optionalCartItem = cartItemsRepository.findByProductIdAndOrderIdAndUserId(
                addProductInCartDto.getProductId(), activeOrder.getId(), addProductInCartDto.getUserId()
        );

        if (optionalProduct.isPresent ( ) && optionalCartItem.isPresent()) {
            CartItems cartItem = optionalCartItem.get();
            Product product = optionalProduct.get();
            activeOrder.setAmount (activeOrder.getAmount () + product.getPrice() );
            activeOrder.setTotalAmount (activeOrder.getTotalAmount () + product.getPrice());
            cartItem.setQuantity(cartItem.getQuantity() + 1);

            cartItemsRepository.save(cartItem);
            orderRepository.save(activeOrder);
            return activeOrder.getOrderDto();
        }
        return null;
    }

    public OrderDto decreaseProductQuantity (AddProductInCartDto addProductInCartDto) {
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus (addProductInCartDto.getUserId(), OrderStatus. Pending);
        Optional <Product> optionalProduct = productRepository.findById(addProductInCartDto.getProductId());

        Optional <CartItems> optionalCartItem = cartItemsRepository.findByProductIdAndOrderIdAndUserId(
                addProductInCartDto.getProductId(), activeOrder.getId(), addProductInCartDto.getUserId()
        );

        if (optionalProduct.isPresent ( ) && optionalCartItem.isPresent()) {
            CartItems cartItem = optionalCartItem.get();
            Product product = optionalProduct.get();
            activeOrder.setAmount (activeOrder.getAmount () - product.getPrice() );
            activeOrder.setTotalAmount (activeOrder.getTotalAmount () - product.getPrice());
            cartItem.setQuantity(cartItem.getQuantity() - 1);

            cartItemsRepository.save(cartItem);
            orderRepository.save(activeOrder);
            return activeOrder.getOrderDto();
        }
        return null;
    }

    public OrderDto placeOrder (PlaceOrderDto placeOrderDto) {
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus (placeOrderDto.getUserId(), OrderStatus. Pending);
       Optional<User> optionalUser = userRepository.findById(placeOrderDto.getUserId());
       if (optionalUser.isPresent()) {
           activeOrder.setOrderDescription(placeOrderDto.getOrderDescription());
           activeOrder.setAddress(placeOrderDto.getAddress());
           activeOrder.setDate(new Date());
           activeOrder.setOrderStatus(OrderStatus.Placed);
           activeOrder.setTrackingId(UUID.randomUUID());

           orderRepository.save(activeOrder);

           Order order = new Order();
           order.setAmount(0L);
           order.setTotalAmount(0L);
           order.setDiscount(0L);
           order.setUser(optionalUser.get());
           order.setOrderStatus(OrderStatus.Pending);
           orderRepository.save(order);

           return activeOrder.getOrderDto();
         }
        return null;
    }

    public List<OrderDto> getMyPlacedOrders(Long userId){

        return orderRepository.findByUserIdAndOrderStatusIn(userId, List.of(OrderStatus. Placed, OrderStatus. Shipped,
                OrderStatus.Delivered)).stream().map(Order::getOrderDto).collect(Collectors.toList());
    }

    public OrderDto searchOrderByTrackingId (UUID trackingId) {
        Optional<Order> optionalOrder = orderRepository.findByTrackingId(trackingId);
        if (optionalOrder.isPresent()) {
            return optionalOrder.get().getOrderDto();
        }
        return null;
    }
}
