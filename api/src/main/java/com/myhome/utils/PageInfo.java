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
 * total pages, and total elements of a given Pageable and Page. It generates a
 * PageInfo object containing these parameters based on input from the Pageable and
 * Page objects. The class also offers a constructor for creating instances of the
 * class from a Pageable and a Page object.
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
   * transforms a `pageable` and a `page` into a `PageInfo` object containing page
   * number, page size, total pages, and total elements.
   * 
   * @param pageable pagination information for the current page of data, providing the
   * page number, page size, total pages, and total elements.
   * 
   * 	- `getPageNumber()`: Returns the page number as an integer value.
   * 	- `getPageSize()`: Returns the page size as an integer value.
   * 	- `getTotalPages()`: Returns the total number of pages as an integer value.
   * 	- `getTotalElements()`: Returns the total number of elements as an integer value.
   * 
   * @param page current page of data to be processed, providing its total elements and
   * pages count.
   * 
   * 	- `pageNumber`: The page number that the input belongs to.
   * 	- `pageSize`: The number of elements in the page.
   * 	- `totalPages`: The total number of pages in the dataset.
   * 	- `totalElements`: The total number of elements in the dataset.
   * 
   * @returns a `PageInfo` object containing page number, page size, total pages, and
   * total elements.
   * 
   * 	- The first parameter, `pageable`, represents the pageable object that contains
   * information about the current page being processed.
   * 	- The second parameter, `page`, represents the actual page data object that is
   * being processed.
   * 	- The return value is a `PageInfo` object, which contains information about the
   * current page, including its page number, page size, total pages, and total elements.
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
