package org.example.services;

import org.example.database.exception.ResourceNotFoundException;
import org.example.database.model.Product;
import org.example.database.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService{

  private final ProductRepository productRepository;

  @Autowired
  public ProductServiceImpl( ProductRepository productRepository){
    this.productRepository = productRepository;
  }

  @Override
  public List<Product> getAllProducts(){
    return productRepository.findAll();
  }

  @Override
  public Optional<Product> getProductById(Long id){
    return productRepository.findById(id);
  }

  @Override
  public Product createProduct(Product product){
    return productRepository.save(product);
  }

  @Override
  public Product updateProduct(Long id, Product productDetails){
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Produkt nicht gefunden mit der ID: " + id));

    product.setName(productDetails.getName());
    product.setPrice(productDetails.getPrice());
    return productRepository.save(product);
  }

  @Override
  public void deleteProduct(Long id){
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Produkt nicht gefunden mit der ID: " + id));
    productRepository.delete(product);
  }
}