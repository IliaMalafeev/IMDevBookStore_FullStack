import { useEffect } from "react";

export const useFetchPaymentFee = (authentication: { isAuthenticated: boolean; token: string; },
                                   setPaymentFees: React.Dispatch<React.SetStateAction<number>>,
                                   setIsLoading: React.Dispatch<React.SetStateAction<boolean>>,
                                   setHttpError: React.Dispatch<React.SetStateAction<string | null>>) => {

    useEffect(

        () => {

            const fetchPaymentFees = async () => {

                setIsLoading(true);

                if (authentication.isAuthenticated) {

                    const baseUrl = `${import.meta.env.VITE_BACKEND_BASE_URL}`;

                    const url = baseUrl + "/payment/secure";

                    const requestOptions = {

                        method: "GET",
                        headers: {
                            Authorization: `Bearer ${authentication.token}`,
                            "Content-type": "application/json"
                        }
                    };

                    const response = await fetch(url, requestOptions);

                    const responseJson = await response.json();

                    if (!response.ok) {
                        throw new Error(responseJson.message ? responseJson.message : "Oops, something went wrong!");
                    }

                    setPaymentFees(responseJson);
                };

                setIsLoading(false);
            };

            fetchPaymentFees().catch(

                (error: any) => {

                    setIsLoading(false);
                    setHttpError(error.message);
                }
            )

        }, []

    );

}