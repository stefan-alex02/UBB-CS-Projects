import React, {memo} from 'react';
import {IonItem, IonLabel} from '@ionic/react';
import {getLogger} from '../core';
import {MovieProps} from './MovieProps';

const log = getLogger('Movie');

interface MoviePropsExt extends MovieProps {
    onEdit: (id?: string) => void,
    color?: string,
    className?: string,
}

const Movie: React.FC<MoviePropsExt> = ({id, title, director, year, rating, onEdit, className}) => {
    return (
        <IonItem className={className} onClick={() => onEdit(id)}>
            <IonLabel className={className}>{title}</IonLabel>
            <IonLabel className={className}>{director}</IonLabel>
            <IonLabel className={className}>{year}</IonLabel>
            <IonLabel className={className}>{rating}</IonLabel>
        </IonItem>
    );
};

export default memo(Movie);
