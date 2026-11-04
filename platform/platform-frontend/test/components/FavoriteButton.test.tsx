import { it, expect, describe, vi } from 'vitest';
import {fireEvent, render, screen} from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import {FavoriteButton} from "../../src/components/FavoriteButton.tsx";

describe('FavoriteButton', () => {
    it('renders should render a IconButton when selected is true', () => {
        const handleClick = vi.fn();
        render(
            <FavoriteButton onClick={handleClick} selected={true}/>
        );

        const button = screen.getByRole('button');
        expect(button).toBeInTheDocument();
    });

    it('renders should render a IconButton when selected is false', () => {
        const handleClick = vi.fn();
        render(
            <FavoriteButton onClick={handleClick} selected={false}/>
        );

        const button = screen.getByRole('button');
        expect(button).toBeInTheDocument();
    });

    it('calls onClick when IconButton is clicked', () => {
        const handleClick = vi.fn();

        render(<FavoriteButton onClick={handleClick} selected={false} mainColor={true} />);

        const button = screen.getByRole('button');
        fireEvent.click(button);

        expect(handleClick).toHaveBeenCalledTimes(1);
    });

});
