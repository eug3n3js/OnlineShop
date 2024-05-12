package cz.cvut.fit.tjv.OnlineShop.controller;

import cz.cvut.fit.tjv.OnlineShop.application.*;
import cz.cvut.fit.tjv.OnlineShop.controller.converter.DTOConverter;
import cz.cvut.fit.tjv.OnlineShop.controller.dto.AvailabilityDTO;
import cz.cvut.fit.tjv.OnlineShop.controller.dto.OfflineShopDTO;
import cz.cvut.fit.tjv.OnlineShop.controller.dto.OrderDTO;
import cz.cvut.fit.tjv.OnlineShop.controller.dto.ProductDTO;
import cz.cvut.fit.tjv.OnlineShop.domain.Availability;
import cz.cvut.fit.tjv.OnlineShop.domain.OfflineShop;
import cz.cvut.fit.tjv.OnlineShop.domain.Order;
import cz.cvut.fit.tjv.OnlineShop.domain.Product;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;

@WebMvcTest(OfflineShopController.class)
public class OfflineShopControllerMvcTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private OfflineShopServiceInterface offlineShopService;
    @MockBean
    private ProductServiceInterface productService;
    @MockBean
    private AvailabilityServiceInterface availabilityService;
    @MockBean
    private OrderServiceInterface orderService;
    @MockBean
    private DTOConverter<OfflineShopDTO, OfflineShop> offlineShopDTOConverter;
    @MockBean
    private DTOConverter<ProductDTO, Product> productDTOConverter;
    @MockBean
    private DTOConverter<AvailabilityDTO, Availability> availabilityDTOConverter;
    @MockBean
    private DTOConverter<OrderDTO, Order> orderDTOConverter;

    @Test
    public void create() throws Exception {
        OfflineShop offlineShopIn = new OfflineShop();
        OfflineShop offlineShopOut = new OfflineShop();
        offlineShopIn.setName("alza");
        offlineShopIn.setAddress("kmochova4");
        offlineShopIn.setOffers(new ArrayList<>());
        offlineShopOut.setId(1l);
        offlineShopOut.setName("alza");
        offlineShopOut.setAddress("kmochova4");
        offlineShopOut.setOffers(new ArrayList<>());
        OfflineShopDTO offlineShopDtoOut = new OfflineShopDTO(1l, "alza", "kmochova4", new ArrayList<>());
        Mockito.when(offlineShopDTOConverter.toEntity(Mockito.any())).thenReturn(offlineShopIn);
        Mockito.when(offlineShopService.createOfflineShop(offlineShopIn)).thenReturn(offlineShopOut);
        Mockito.when(offlineShopDTOConverter.toDTO(offlineShopOut)).thenReturn(offlineShopDtoOut);
        String content = "{\n\"name\":\"alza\",\"address\":\"kmochova4\",\"offerIds\":[]}";
        mockMvc.perform(MockMvcRequestBuilders
                .post("/rest/api/shop")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)));
    }
}
