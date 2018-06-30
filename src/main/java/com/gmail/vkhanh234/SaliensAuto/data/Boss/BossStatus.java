package com.gmail.vkhanh234.SaliensAuto.data.Boss;

import java.util.List;

public class BossStatus {
    public long boss_hp,boss_max_hp,num_laser_uses,num_team_heals;
    public List<BossPlayer> boss_players;
    public boolean game_over,waiting_for_players;
}
