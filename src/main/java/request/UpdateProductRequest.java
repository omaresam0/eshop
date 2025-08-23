package request;

import com.eshop.eshop.model.Category;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateProductRequest {
    private Long id;
    private String name;
    private String brand;
    private String description;
    private int stockCount; // stock quantity
    private BigDecimal price;
    private Category category;
}
