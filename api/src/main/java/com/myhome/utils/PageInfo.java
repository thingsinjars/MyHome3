package com.myhome.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * from the file is a data structure that contains information about a page of results
 * in a larger dataset. It has four fields: currentPage, pageLimit, totalPages, and
 * totalElements. The class also provides a constructor for creating instances of the
 * class from a Pageable object and a Page object.
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
   * creates a `PageInfo` object containing information about the number of pages, page
   * size, total pages, and total elements of a `Pageable` and a `Page`.
   * 
   * @param pageable pagination information for the current page of data, providing the
   * page number, page size, total pages, and total elements.
   * 
   * 	- `pageable.getPageNumber()`: This represents the page number in the paginated
   * sequence of the input.
   * 	- `pageable.getPageSize()`: This denotes the number of elements that can be
   * displayed on a single page of the input.
   * 	- `page.getTotalPages()`: This indicates the total number of pages in the entire
   * dataset of the input.
   * 	- `page.getTotalElements()`: This represents the total number of elements in the
   * entire dataset of the input.
   * 
   * @param page current page being processed, providing information on its position
   * and size within the overall paginated result.
   * 
   * 	- `pageNumber`: The page number of the paginated result.
   * 	- `pageSize`: The number of elements in each page of the result.
   * 	- `totalPages`: The total number of pages in the result.
   * 	- `totalElements`: The total number of elements in the entire result.
   * 
   * @returns a `PageInfo` object containing page number, page size, total pages, and
   * total elements.
   * 
   * 	- The page number is the zero-based index of the current page among all pages in
   * the collection.
   * 	- Page size refers to the number of elements in each page.
   * 	- Total pages refer to the total number of pages in the collection.
   * 	- Total elements refer to the total number of elements in the entire collection.
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
