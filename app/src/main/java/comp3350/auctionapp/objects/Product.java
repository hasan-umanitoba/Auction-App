package comp3350.auctionapp.objects;

import androidx.annotation.Nullable;

import java.io.Serializable;

import comp3350.auctionapp.objects.enums.Category;

import static java.util.Objects.requireNonNull;

/**
 * Product
 * Holds information about a single Product
 */
public class Product implements Serializable {
    //Limits how long a title and a description can be
    public static final int MAX_NAME_LEN = 100;
    public static final int MAX_DESC_LENGTH = 300;

    private String name;
    private String description;
    private Category category;
    private String imageFileName; //Will just be using images in res for now

    public Product(String name, String description, Category category, String imageFileName) {
        requireNonNull(name, "Name can't be null");
        requireNonNull(description, "description can't be null");

        if (name.length() > MAX_NAME_LEN || name.isEmpty()) {
            throw new IllegalArgumentException("Invalid name");
        } else if (description.length() > MAX_DESC_LENGTH) {
            throw new IllegalArgumentException("Invalid description");
        }

        this.name = name;
        this.description = description;
        this.category = requireNonNull(category, "Category can't be null");
        this.imageFileName = requireNonNull(imageFileName);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Product) {
            return this.name.equals(((Product) obj).name) &&
                    this.description.equals(((Product) obj).description) &&
                    this.category.equals(((Product) obj).category) &&
                    this.imageFileName.equals(((Product) obj).imageFileName);
        }
        return false;
    }
}
