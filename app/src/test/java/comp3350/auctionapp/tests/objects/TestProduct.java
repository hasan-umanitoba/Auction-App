package comp3350.auctionapp.tests.objects;

import org.junit.Test;

import comp3350.auctionapp.objects.Product;
import comp3350.auctionapp.objects.enums.Category;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

public class TestProduct {
    @Test
    public void testCreateValidProduct() {
        Product newProduct = new Product("My product", "My description", Category.COLLECTIBLES, "image.jpg");

        assertEquals("Should have same product name", "My product", newProduct.getName());
        assertEquals("Should have same product description", "My description", newProduct.getDescription());
        assertEquals("Should have same category", Category.COLLECTIBLES, newProduct.getCategory());
        assertEquals("Should have same image name", "image.jpg", newProduct.getImageFileName());
    }

    @Test(expected = NullPointerException.class)
    public void testNoName() {
        new Product(null, "My description", Category.COLLECTIBLES, "image.jpg");
    }

    @Test(expected = NullPointerException.class)
    public void testNoDesc() {
        new Product("My product", null, Category.COLLECTIBLES, "image.jpg");
    }

    @Test(expected = NullPointerException.class)
    public void testNoCategory() {
        new Product("My product", "My description", null, "image.jpg");
    }

    @Test(expected = NullPointerException.class)
    public void testNoImage() {
        new Product("My product", "My description", Category.COLLECTIBLES, null);
        fail("Should have thrown exception");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithEmptyName() {
        new Product("", "My description", Category.COLLECTIBLES, "image.jpg");
        fail("Should have thrown exception");
    }

    @Test
    public void testCreateWithEmptyDesc() {
        Product newProduct = new Product("Product", "", Category.COLLECTIBLES, "image.jpg");
        assertEquals("Should have no description", "", newProduct.getDescription());
    }

    @Test
    public void testCreateWithEmptyFileName() {
        Product newProduct = new Product("Product", "description", Category.COLLECTIBLES, "");
        assertEquals("Should have no picture name", "", newProduct.getImageFileName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLongName() {
        new Product("This is a long item name This is a long item name This is a long item name This is a long item name This is a long item name This is a long item name This is a long item name",
                "My description", Category.COLLECTIBLES, "image.jpg");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLongDesc() {
        new Product("My product",
                "This is a very long description This is a very long description This is a very long description This is a very long description This is a very long description This is a very long description This is a very long description This is a very long description This is a very long description This is a very long description",
                Category.COLLECTIBLES, "image.jpg");
    }

    @Test
    public void testEqualProducts() {
        Product product1 = new Product("Name", "Description", Category.JEWELLERY, "file.jpg");
        Product product2 = new Product("Name", "Description", Category.JEWELLERY, "file.jpg");
        assertEquals("Products should be equal", product1, product2);
    }

    @Test
    public void testUnequalTitles() {
        Product product1 = new Product("Name1", "Description", Category.JEWELLERY, "file.jpg");
        Product product2 = new Product("Name", "Description", Category.JEWELLERY, "file.jpg");
        assertNotEquals("Products should not be equal", product1, product2);
    }

    @Test
    public void testUnequalDesc() {
        Product product1 = new Product("Name", "Description1", Category.JEWELLERY, "file.jpg");
        Product product2 = new Product("Name", "Description", Category.JEWELLERY, "file.jpg");
        assertNotEquals("Products should not be equal", product1, product2);
    }

    @Test
    public void testUnequalCategories() {
        Product product1 = new Product("Name", "Description", Category.JEWELLERY, "file.jpg");
        Product product2 = new Product("Name", "Description", Category.ELECTRONICS, "file.jpg");
        assertNotEquals("Products should not be equal", product1, product2);
    }

    @Test
    public void testUnequalFileName() {
        Product product1 = new Product("Name", "Description", Category.JEWELLERY, "file.jpg");
        Product product2 = new Product("Name", "Description", Category.JEWELLERY, "file2.jpg");
        assertNotEquals("Products should not be equal", product1, product2);
    }

    @Test
    public void testEqualWithObj() {
        Product product1 = new Product("Name", "Description", Category.JEWELLERY, "file.jpg");
        assertNotEquals("Should not be equal", product1, new Object());
    }
}
