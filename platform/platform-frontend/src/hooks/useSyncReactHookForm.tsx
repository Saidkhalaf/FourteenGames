import { useEffect } from "react";
import type { FieldValues, UseFormReset } from "react-hook-form";

interface ReactHookFormStateProps<T extends FieldValues> {
    syncDependency: unknown;
    reset: UseFormReset<T>;
    values: T;
}

export function useSyncReactHookForm<T extends FieldValues>({syncDependency, reset, values}: ReactHookFormStateProps<T>) {
    useEffect(() => {
        if (syncDependency) {
            reset(values);
        }
    }, [syncDependency, reset, values]);
}
