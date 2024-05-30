package com.myhome.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * is a data structure that provides information about the number of pages, page size,
 * total pages, and total elements of a given Pageable and Page. The class provides
 * a constructor for creating instances of the class from a Pageable and a Page object,
 * and also offers a static method for generating a `PageInfo` object based on a
 * Pageable and a Page object.
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
   * generates a `PageInfo` object from a `Pageable` and a `Page`. It returns information
   * about the number of pages, page size, total pages, and total elements in the page.
   * 
   * @param pageable pageable object that contains information about the current page
   * being processed, including the page number and the number of elements per page.
   * 
   * 	- The `getPageNumber()` method returns the current page number being processed.
   * 	- The `getPageSize()` method returns the number of elements in each page.
   * 	- The `getTotalPages()` method returns the total number of pages that contain elements.
   * 	- The `getTotalElements()` method returns the total number of elements in all pages.
   * 
   * @param page current page of elements being processed, providing the total number
   * of elements and pages available for the pageable object.
   * 
   * 	- The `getPageNumber()` method returns the current page number of the paginated
   * sequence.
   * 	- The `getPageSize()` method returns the size of each page in the paginated sequence.
   * 	- The `getTotalPages()` method returns the total number of pages in the paginated
   * sequence.
   * 	- The `getTotalElements()` method returns the total number of elements in the
   * paginated sequence.
   * 
   * @returns a `PageInfo` object containing various pagination-related metadata.
   * 
   * 	- `pageNumber`: The number of the current page being displayed.
   * 	- `pageSize`: The number of elements displayed on each page.
   * 	- `totalPages`: The total number of pages in the dataset.
   * 	- `totalElements`: The total number of elements in the dataset.
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
