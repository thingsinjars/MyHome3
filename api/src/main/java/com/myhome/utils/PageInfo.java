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
 * PageInfo object containing these values based on the provided Pageable and Page objects.
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
   * generates a `PageInfo` object containing information about the number of pages,
   * page size, total pages, and total elements for a given `Pageable` and `Page`.
   * 
   * @param pageable pageable object that contains information about the current page
   * of data being processed, including the page number and size.
   * 
   * 	- The `pageNumber` property represents the number of the page being returned.
   * 	- The `pageSize` property indicates the number of elements in each page.
   * 	- The `totalPages` property holds the total number of pages available for a
   * particular query or result set.
   * 	- The `totalElements` property shows the overall quantity of elements in the
   * result set.
   * 
   * @param page current page being processed, providing information on its position,
   * size, total pages, and total elements.
   * 
   * 	- `pageNumber`: The page number associated with the pageable object.
   * 	- `pageSize`: The size of each page in the pageable object.
   * 	- `totalPages`: The total number of pages associated with the pageable object.
   * 	- `totalElements`: The total number of elements associated with the pageable object.
   * 
   * @returns a `PageInfo` object containing various metrics related to the given
   * pageable and page.
   * 
   * 1/ `pageNumber`: The page number of the pageable object.
   * 2/ `pageSize`: The size of each page in the pageable object.
   * 3/ `totalPages`: The total number of pages in the pageable object.
   * 4/ `totalElements`: The total number of elements in the pageable object.
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
