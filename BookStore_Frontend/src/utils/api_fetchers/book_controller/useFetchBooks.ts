import { useEffect } from "react";
import { BookModel } from "../../../models/BookModel";

export const useFetchBooks = (urlPaginationParams: string, 
                              currentPage: number,
                              setBooks: React.Dispatch<React.SetStateAction<BookModel[]>>,
                              setIsLoading: React.Dispatch<React.SetStateAction<boolean>>,
                              setHttpError: React.Dispatch<React.SetStateAction<string | null>>,
                              setTotalAmountOfBooks?: React.Dispatch<React.SetStateAction<number>>,
                              setTotlalPages?: React.Dispatch<React.SetStateAction<number>>,
                              urlSearchParams?: string,
                              isBookDeleted?: boolean) => {

    useEffect(

        () => {

            const fetchBooks = async () => {

                setIsLoading(true);

                const baseUrl = `${import.meta.env.VITE_BACKEND_BASE_URL}`;

                const url: string = baseUrl + "/books" + (urlSearchParams ? urlSearchParams : "") + urlPaginationParams;

                const response = await fetch(url);

                const responseJson = await response.json();

                if (!response.ok) {
                    throw new Error(responseJson.message ? responseJson.message : "Oops, something went wrong!");
                }

                if (setTotalAmountOfBooks) setTotalAmountOfBooks(responseJson.totalElements);
                if (setTotlalPages) setTotlalPages(responseJson.totalPages);

                const responseBooksContentArray = responseJson.content;

                const loadedBooks: BookModel[] = [];

                for (const key in responseBooksContentArray) {

                    loadedBooks.push(responseBooksContentArray[key]);
                }

                setBooks(loadedBooks);
                setIsLoading(false);
            }

            fetchBooks().catch(

                (error: any) => {

                    setIsLoading(false);
                    setHttpError(error.message);
                }
            )

        }, [currentPage, urlSearchParams, isBookDeleted]

    );

}