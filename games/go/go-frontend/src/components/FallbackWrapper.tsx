import {type PropsWithChildren, type ReactNode, Suspense} from "react";
import {ErrorBoundary} from "react-error-boundary";

interface FallbackWrapperProps {
    loadingFallback: ReactNode;
    errorFallback: ReactNode;
}

export function FallbackWrapper({loadingFallback, errorFallback, children}: PropsWithChildren<FallbackWrapperProps>
) {
    return (
        <ErrorBoundary fallback={errorFallback}>
            <Suspense fallback={loadingFallback}>
                {children}
            </Suspense>
        </ErrorBoundary>
    )
}