import {RouteGuard} from "../identity/RouteGuard.tsx";
import {FallbackWrapper} from "../FallbackWrapper.tsx";
import type {ReactNode} from "react";

interface createFallBackWrapperProps {
    children: ReactNode,
    loadingFallback: ReactNode,
    errorFallback: ReactNode
}

export function createFallbackWrapper({children, loadingFallback, errorFallback}: createFallBackWrapperProps) {
    return (
        <FallbackWrapper loadingFallback={loadingFallback}
                         errorFallback={errorFallback}>
            {children}
        </FallbackWrapper>
    )
}

export function createFallbackWrapperWithRouteGuard({children,loadingFallback,errorFallback}: createFallBackWrapperProps) {
    return (
        <RouteGuard>
            <FallbackWrapper loadingFallback={loadingFallback}
                             errorFallback={errorFallback}>
                {children}
            </FallbackWrapper>
        </RouteGuard>
    )
}