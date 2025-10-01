package elabbora.backend.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
public class PageResponseDTO<T> {
    @JsonView(Object.class)
    private List<T> content;

    @JsonView(Object.class)
    private final int number;

    @JsonView(Object.class)
    private final int size;

    @JsonView(Object.class)
    private final long totalElements;

    @JsonView(Object.class)
    private final int totalPages;

    public PageResponseDTO(Page<T> page) {
        this.content = page.getContent();
        this.number = page.getNumber();
        this.size = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
    }
}
