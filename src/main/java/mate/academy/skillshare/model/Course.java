package mate.academy.skillshare.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String author;
    @Column(nullable = false)
    private String title;
    private String cardImage;
    @Column(nullable = false)
    private String duration;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CourseType type;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Format format;
    @Column(nullable = false)
    private boolean certificate;
    @Column(nullable = false)
    private boolean trial;
    @Column(nullable = false)
    private BigDecimal price;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Content> contents = new LinkedHashSet<>();
    @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Image> images = new LinkedHashSet<>();
    private String source;
    @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @OrderBy("timestamp DESC")
    private List<Review> reviews = new ArrayList<>();

    public enum CourseType {
        ONLINE,
        OFFLINE
    }

    public enum Format {
        GROUP,
        PERSONAL
    }
}
