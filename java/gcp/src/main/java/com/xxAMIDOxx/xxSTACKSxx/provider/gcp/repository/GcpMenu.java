package com.xxAMIDOxx.xxSTACKSxx.provider.gcp.repository;

import com.google.cloud.firestore.annotation.DocumentId;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cloud.gcp.data.firestore.Document;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collectionName = "Menu")
public class GcpMenu {

  @Id
  @DocumentId
  private String id;

  private String restaurantId;

  private String name;

  private String description;

  @Builder.Default private List<Category> categories = new ArrayList<>();

  private Boolean enabled;

  public GcpMenu addOrUpdateCategory(Category category) {
    if (this.categories == null) {
      this.categories = new ArrayList<>();
    }
    this.categories =
        this.categories.stream()
            .filter(c -> !c.getId().equals(category.getId()))
            .collect(Collectors.toList());
    this.categories.add(category);
    return this;
  }
}
