package com.myhome.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * provides information about the number of pages, page size, total pages, and total
 * elements of a given Pageable and Page object. The class offers a constructor for
 * creating instances of the class from a Pageable and a Page object, as well as a
 * static method for generating a PageInfo object based on these input parameters.
 */
@EqualsAndHashCode
@ToString
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PageInfo {
  private final int currentPage;
  private final int pageLimit;
  private final int totalPages;
  private final long totalElements;

  /**
   * creates a `PageInfo` object from a `Pageable` and a `Page`. It returns information
   * about the number of pages, page size, total pages, and total elements in the page.
   * 
   * @param pageable Pageable interface, which provides the ability to page data.
   * 
   * 	- `getPageNumber(): int`: The number of the current page being accessed.
   * 	- `getPageSize(): int`: The size of each page in terms of the number of elements
   * it can accommodate.
   * 	- `getTotalPages(): int`: The total number of pages available for the given input.
   * 	- `getTotalElements(): long`: The total number of elements that can be accessed
   * through the pageable input.
   * 
   * @param page current page being processed, providing the total number of elements
   * on that page.
   * 
   * 	- `pageNumber`: The number of the page being referenced.
   * 	- `pageSize`: The size of each page in the paginated sequence.
   * 	- `totalPages`: The total number of pages in the paginated sequence.
   * 	- `totalElements`: The total number of elements in the paginated sequence.
   * 
   * @returns a `PageInfo` object containing information about the number of pages,
   * page size, total pages, and total elements of a pageable.
   * 
   * 	- pageable.getPageNumber(): This represents the current page number being viewed.
   * 	- pageable.getPageSize(): This indicates the number of elements displayed on each
   * page.
   * 	- page.getTotalPages(): This indicates the total number of pages in the dataset.
   * 	- page.getTotalElements(): This shows the overall number of elements in the dataset.
   */
  public static PageInfo of(Pageable pageable, Page<?> page) {
    return new PageInfo(
        pageable.getPageNumber(),
        pageable.getPageSize(),
        page.getTotalPages(),
        page.getTotalElements()
    );
  }
}
