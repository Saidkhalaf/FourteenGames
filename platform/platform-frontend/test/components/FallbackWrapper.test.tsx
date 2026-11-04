import { it, expect, describe } from 'vitest';
import { render, screen } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import { FallbackWrapper } from '../../src/components/FallbackWrapper';

describe('FallbackWrapper', () => {
    it('renders children when nothing suspends', () => {
        render(
            <FallbackWrapper
                loadingFallback={<div>Loading</div>}
                errorFallback={<div>Error</div>}
            >
                <h1>Hello test!</h1>
            </FallbackWrapper>,
        );

        const heading = screen.getByRole('heading');
        expect(heading).toBeInTheDocument();
        expect(heading).toHaveTextContent('Hello test!');
    });

    it('renders errorfallback when an error is thrown.', () => {
        function ErrorComponent(){
            throw new Error("Test Error");
            return (<h1>ErrorComponent</h1>)
        }

        const {container} = render(
            <FallbackWrapper
                loadingFallback={<div>Loading</div>}
                errorFallback={<div>Error</div>}
            >
                <ErrorComponent/>
            </FallbackWrapper>,
        );

        const div = container.querySelector('div');
        expect(div).toBeInTheDocument();
        expect(div).toHaveTextContent('Error');
    });

});
