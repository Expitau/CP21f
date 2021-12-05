#include <iostream>
#include <ctime>
#include "player.h"

void round(Player & a, Player & b);

int main() {
    std::srand(std::time(nullptr));

    Player a, b;
    a.add_monster(fireMon);
    a.add_monster(fireMon);
    a.add_monster(waterMon);
    b.add_monster(waterMon);
    b.add_monster(grassMon);
    b.add_monster(grassMon);

    std::cout << "Game start! Player A: " << a.get_total_hp() << ", Player B: " << b.get_total_hp() << std::endl;

    for (int i = 1;; i++) {
        round(a, b);
        std::cout << "Round " << i << ": " << a.get_total_hp() << " " << b.get_total_hp() << std::endl;
        if (b.get_num_monsters() == 0) {
            std::cout << "Player a won the game!" << std::endl;
            break;
        } else if (a.get_num_monsters() == 0) {
            std::cout << "Player b won the game!" << std::endl;
            break;
        }
    }
    return 0;
}


void round(Player & a, Player & b) {
    Monster * am = a.select_monster();
    Monster * bm = b.select_monster();

    cout << *am << " " << *bm << endl;

    if(am->get_speed() > bm->get_speed()) {
        am->attack(bm);
        if(bm->get_hp() <= 0){
            std::cout << "b's poketmon " << *bm << " fainted" << std::endl;
            b.delete_monster(bm);
            return;
        }
        bm->attack(am);
        if(am->get_hp() <= 0){
            std::cout << "a's poketmon " << *am << " fainted" << std::endl;
            a.delete_monster(am);
            return;
        }
    }else{
        bm->attack(am);
        if(am->get_hp() <= 0){
            std::cout << "a's poketmon " << *am << " has fainted" << std::endl;
            a.delete_monster(am);
            return;
        }

        am->attack(bm);
        if(bm->get_hp() <= 0) {
            std::cout << "b's poketmon " << *bm << " has fainted" << std::endl;
            b.delete_monster(bm);
            return;
        }
    }
}
